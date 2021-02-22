package com.vocalink.crossproduct.infrastructure.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Order(2)
public class CorrelationIdPostFilter implements Filter {

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

    if (servletResponse instanceof HttpServletResponse) {
      HttpServletResponse res = (HttpServletResponse) servletResponse;
      String requestCid = MDC.get(mdcKey);
      if (requestCid != null) {
        res.addHeader(correlationHeaderName, requestCid);
      }
    }
    chain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {
    //No op
  }
}
