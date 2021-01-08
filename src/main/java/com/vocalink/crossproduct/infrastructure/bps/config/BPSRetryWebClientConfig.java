package com.vocalink.crossproduct.infrastructure.bps.config;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class BPSRetryWebClientConfig {

  private final BPSProperties bpsProperties;

  public final Retry fixedRetry() {
    return Retry.fixedDelay(bpsProperties.getRetryCount(), bpsProperties.getTimeoutDuration())
        .filter(this::worthRetrying)
        .onRetryExhaustedThrow(
            (retryBackoffSpec, retrySignal) ->
              new AdapterException("Timeout!", retrySignal.failure(), "BPS"));
  }

  private boolean worthRetrying(Throwable throwable) {
    if (throwable instanceof WebClientResponseException) {
      final WebClientResponseException responseException = (WebClientResponseException) throwable;
      return bpsProperties.getRetryable().contains(responseException.getRawStatusCode());
    }
    return false;
  }
}
