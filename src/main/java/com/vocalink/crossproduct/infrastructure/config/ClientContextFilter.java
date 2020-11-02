package com.vocalink.crossproduct.infrastructure.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ClientContextFilter extends OncePerRequestFilter {

  @Value("${app.context}")
  private Set<String> contexts;

  @Value("${app.infra-endpoints}")
  private String[] whitelistedEndpoints;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {
    log.debug("Applying filter for : {}", request.getRequestURI());

    String contextHeader = request.getHeader("context");

    if (!contexts.contains(contextHeader)) {
      throw new IllegalArgumentException("Missing or invalid request header 'context': " + contextHeader);
    }
    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return Arrays.stream(whitelistedEndpoints)
      .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
  }
}
