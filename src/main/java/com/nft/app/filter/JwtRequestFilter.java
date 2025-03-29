package com.nft.app.filter;

import com.nft.app.dto.NftResponse;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
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

    if (token == null) {
      throw new NftException(ErrorCode.INVALID_TOKEN);
    }

    try {
      email = jwtUtil.extractEmail(token);

//    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (email != null) {
        if (jwtUtil.validateToken(token, email)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
          logger.info("Token verification failed");
          throw new RuntimeException("Invalid token");
        }
      }
    } catch (Exception e) {
      NftResponse<Object> nftResponse = new NftResponse<>(ErrorCode.INVALID_TOKEN);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(ErrorCode.INVALID_TOKEN.getHttpStatus().value());
      response.getWriter().write(JsonUtils.convertObjectToJson(nftResponse));
      return;
    }

    chain.doFilter(request, response);
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
