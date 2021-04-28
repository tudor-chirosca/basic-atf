package com.vocalink.crossproduct.infrastructure.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientTypeFilter extends AppFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {
    log.debug("Applying filter for : {}", request.getRequestURI());

    if (request.getMethod().equals("GET") || request.getMethod().equals("POST")) {
      String clientType = request.getHeader("client-type");

      if (EnumUtils.getEnum(ClientType.class, clientType) == null) {
        throw new ClientRequestException(BAD_REQUEST,
            "Missing or invalid request header 'client-type': " + clientType);
      }
    }

    chain.doFilter(request, response);
  }
}
