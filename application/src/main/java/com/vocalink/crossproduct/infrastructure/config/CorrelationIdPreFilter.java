package com.vocalink.crossproduct.infrastructure.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
public class CorrelationIdPreFilter implements Filter
{

  @Value("${app.logging.correlation.mdc}")
  private String mdcKey;

  @Value("${app.logging.correlation.header}")
  private String correlationHeaderName;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    //No op
  }

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {

    if (servletRequest instanceof HttpServletRequest) {
      HttpServletRequest req = (HttpServletRequest) servletRequest;
      String requestCid = req.getHeader(correlationHeaderName);

      if (requestCid == null) {
        requestCid = generateCorrelationId();
      }
      log.debug("Tracked request with correlation id {}", requestCid);
      MDC.put(mdcKey, requestCid);
      try {
        chain.doFilter(servletRequest, servletResponse);
      } finally {
        MDC.remove(mdcKey);
      }
    }
  }

  @Override
  public void destroy() {
    //No op
  }

  private String generateCorrelationId(){
    return java.util.UUID.randomUUID().toString();
  }
}
