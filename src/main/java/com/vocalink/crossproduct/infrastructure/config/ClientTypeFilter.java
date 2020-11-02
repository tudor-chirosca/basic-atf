package com.vocalink.crossproduct.infrastructure.config;

import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ClientTypeFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {
    log.debug("Applying filter for : {}", request.getRequestURI());

    String clientType = request.getHeader("client-type");

    if (EnumUtils.getEnum(ClientType.class, clientType) == null) {
      throw new IllegalArgumentException("Missing or invalid request header 'client-type': " + clientType);
    }
    chain.doFilter(request, response);
  }
}
