package com.vocalink.crossproduct.infrastructure.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import java.io.IOException;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClientContextFilter extends AppFilter {

  @Value("${app.context}")
  private Set<String> contexts;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws ServletException, IOException {
    log.debug("Applying filter for : {}", request.getRequestURI());

    if (request.getMethod().equals("GET") || request.getMethod().equals("POST")) {
      String contextHeader = request.getHeader("context");

      if (!contexts.contains(contextHeader)) {
        throw new ClientRequestException(BAD_REQUEST,
            "Missing or invalid request header 'context': " + contextHeader);
      }
    }

    chain.doFilter(request, response);
  }
}
