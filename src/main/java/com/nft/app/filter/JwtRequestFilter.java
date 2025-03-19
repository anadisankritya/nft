package com.nft.app.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.NftException;
import com.nft.app.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
      "/nft/user/api/v1/login"
  );

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    final String token = request.getHeader("Authorization");

    String email;

    if (token == null) {
      throw new NftException(ErrorCode.GENERIC_EXCEPTION);
    }

    try {
      email = jwtUtil.extractEmail(token);
    } catch (JWTVerificationException e) {
      logger.error("Token verification failed", e);
      throw new RuntimeException("Unable to extract token");
    }

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
