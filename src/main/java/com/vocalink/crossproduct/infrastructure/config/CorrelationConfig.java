package com.vocalink.crossproduct.infrastructure.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Configuration
public class CorrelationConfig {

  /**
   * Returns filtering function that is getting MDC value for correlation ID
   * and setting it on the downstream http request.
   * <p>
   * Filter is executed twice for request and response
   * Initial request is executed on the requester thread, thus it is safe to use MDC within it
   * In case MDC will be accessed in a flatMap instruction or some downstream operation
   * it will be on a Reactor thread and MDC will not have the correct value set.
   * </p>
   */
  @Bean
  public ExchangeFilterFunction correlationFilterFunction(
      @Value("${app.logging.correlation.header}") String correlationHeaderName,
      @Value("${app.logging.correlation.mdc}") String mdcKey
  ) {
    return (request, next) -> {
      String correlationId = MDC.get(mdcKey);
      ClientRequest newRequest = request;
      if (correlationId != null) {
        newRequest = ClientRequest.from(request)
            .header(correlationHeaderName, correlationId)
            .build();
      }
      return next.exchange(newRequest);
    };
  }

}
