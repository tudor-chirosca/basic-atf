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
  protected String[] whitelistedEndpoints;

  @Value("${server.servlet.context-path}")
  protected String contextPath;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    final String requestPath = request.getRequestURI().replace(contextPath, "");

    return Arrays.stream(whitelistedEndpoints)
        .anyMatch(e -> new AntPathMatcher().match(e, requestPath));
  }
}
