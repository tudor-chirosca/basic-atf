package com.vocalink.crossproduct.infrastructure.bps.alert;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.ALERTS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.ALERT_STATS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.ALERT_THRESHOLD_PATH;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.alert.AlertRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSAlertRepository implements AlertRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public AlertReferenceData findAlertsReferenceData() {
    return webClient.post()
        .uri(resolve(ALERT_THRESHOLD_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(BPSAlertReferenceData.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toEntity)
        .block();
  }

  @Override
  public AlertStats findAlertStats() {
    return webClient.post()
        .uri(resolve(ALERT_STATS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(BPSAlertStats.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toEntity)
        .block();
  }

  @Override
  public Page<Alert> findPaginated(AlertSearchCriteria criteria) {
    BPSAlertSearchRequest request = BPSMAPPER.toBps(criteria);
    return webClient.post()
        .uri(resolve(ALERTS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSAlertSearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSAlert>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toAlertPageEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
