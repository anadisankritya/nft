package com.nft.app.config;

import com.nft.app.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtRequestFilter jwtRequestFilter;

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .csrf(AbstractHttpConfigurer::disable)
//        .cors(AbstractHttpConfigurer::disable)
//        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//        .authorizeHttpRequests(auth -> auth
//            .requestMatchers(HttpMethod.POST, "/nft/register/api/v1/send-email-otp").permitAll()
//            .anyRequest().authenticated())
//        .exceptionHandling(Customizer.withDefaults())
//        .sessionManagement(session ->
//            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .httpBasic(Customizer.withDefaults());
//    return http.build();
//  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
  }

}
