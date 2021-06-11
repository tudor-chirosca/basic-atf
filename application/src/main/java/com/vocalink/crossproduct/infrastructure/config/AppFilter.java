package com.vocalink.crossproduct.infrastructure.config;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

abstract class AppFilter extends OncePerRequestFilter {

  @Value("${app.infra-endpoints}")
  private String[] whitelistedEndpoints;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return Arrays.stream(whitelistedEndpoints)
        .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
  }
}
