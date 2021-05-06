package com.vocalink.crossproduct.infrastructure.bps.reference;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.MESSAGE_DIRECTION_REFERENCES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.REASON_CODES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.STATUSES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference;
import com.vocalink.crossproduct.domain.reference.ReferencesRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class BPSReferenceRepository implements ReferencesRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  public List<MessageDirectionReference> findMessageDirectionReferences() {
    return webClient.post()
        .uri(resolve(MESSAGE_DIRECTION_REFERENCES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToFlux(BPSMessageDirectionReference.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public ReasonCodeReference findReasonCodeReference() {
    return webClient.post()
        .uri(resolve(REASON_CODES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(BPSReasonCodeReference.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public List<String> findStatuses(String enquiryType) {
    return webClient.post()
        .uri(resolve(STATUSES_PATH, bpsProperties,
            bpsProperties.getSchemeCode(), BPSEnquiryType.valueOf(enquiryType).name()))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StatusWrapper>>() {})
        .map(w -> w.stream().map(StatusWrapper::getStatus).collect(Collectors.toList()))
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
