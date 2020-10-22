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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component("applicationContextFilter")
public class ClientContextFilter extends OncePerRequestFilter {

  @Value("${app.context}")
  private Set<String> contexts;

  @Value("${app.infra-endpoints}")
  private String[] whitelistedEndpoints;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain)
    throws ServletException, IOException {
    log.info("Applying filter for : {}",
        httpServletRequest.getRequestURI());

    String contextHeader = httpServletRequest.getHeader("context");
    if (contexts.contains(contextHeader)) {
      chain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }
    httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid context");
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return Arrays.stream(whitelistedEndpoints)
      .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
  }
}
