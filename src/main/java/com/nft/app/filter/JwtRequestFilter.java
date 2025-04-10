package com.nft.app.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nft.app.dto.NftResponse;
import com.nft.app.entity.UserToken;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.repository.UserTokenRepository;
import com.nft.app.util.JsonUtils;
import com.nft.app.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final UserTokenRepository userTokenRepository;

  private static final List<String> BYPASS_URI_LIST = List.of(
      "/nft/register/api/v1/send-email-otp",
      "/nft/register/api/v1/send-phone-otp",
      "/nft/register/api/v1/signup",
      "/nft/user/api/v1/login",
      "/nft/ui/**"
  );

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String token = request.getHeader("Authorization");

    String email;

    try {

      if (token == null) {
        throw new NftException(ErrorCode.INVALID_TOKEN);
      }

      email = JwtUtil.extractEmail(token);

//    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (email != null) {
        if (JwtUtil.validateToken(token, email)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
          Optional<UserToken> userTokenOptional = userTokenRepository.findByToken(token);
          if (userTokenOptional.isEmpty() || !userTokenOptional.get().isActive()) {
            throw new TokenExpiredException("Token Expired", Instant.now());
          }
        } else {
          log.info("Token verification failed");
          throw new NftException(ErrorCode.INVALID_TOKEN);
        }
      }
    } catch (TokenExpiredException ex) {
      generateExceptionResponse(ErrorCode.TOKEN_EXPIRED, response);
      return;
    } catch (Exception e) {
      generateExceptionResponse(ErrorCode.INVALID_TOKEN, response);
      return;
    }

    chain.doFilter(request, response);
  }

  private static void generateExceptionResponse(ErrorCode tokenExpired, HttpServletResponse response) throws IOException {
    NftResponse<Object> nftResponse = new NftResponse<>(tokenExpired);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(ErrorCode.INVALID_TOKEN.getHttpStatus().value());
    response.getWriter().write(JsonUtils.convertObjectToJson(nftResponse));
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    PathMatcher pathMatcher = new AntPathMatcher();
    String requestURI = request.getRequestURI();
    String host = request.getHeader("host");
    log.info("checking shouldNotFilter for URI - {}, host - {}", requestURI, host);
    return BYPASS_URI_LIST.stream().anyMatch(bypassUri -> pathMatcher.match(bypassUri, requestURI));
  }

}
