package com.vocalink.crossproduct.infrastructure.config;

import java.io.IOException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component("applicationContextFilter")
public class ClientContextFilter implements Filter {

  @Value("${app.context}")
  private Set<String> contexts;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    //no implementation
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    log.info("Applying filter for : {}",
        httpServletRequest.getRequestURI());

    String contextHeader = httpServletRequest.getHeader("context");

    if (!contexts.contains(contextHeader) &&
        !(httpServletRequest.getServletPath().contains("/actuator/health"))) {
      httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid context");
    } else {
      chain.doFilter(request, response);
      log.info("Passing filter validation : {}",
          httpServletRequest.getRequestURI());
    }
  }

  @Override
  public void destroy() {
    //no implementation
  }

}
