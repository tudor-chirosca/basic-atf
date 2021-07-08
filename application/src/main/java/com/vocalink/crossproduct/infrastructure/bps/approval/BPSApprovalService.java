package com.vocalink.crossproduct.infrastructure.bps.approval;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.APPROVAL_CREATE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalService;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSApprovalService implements ApprovalService {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public ApprovalConfirmationResponse submitApprovalConfirmation(
      ApprovalConfirmation approvalConfirmation) {
    final BPSApprovalConfirmationRequest bpsRequest = BPSMAPPER.toBps(approvalConfirmation);
    return webClient.put()
        .uri(resolve(APPROVAL_CREATE_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSApprovalConfirmationRequest.class))
        .retrieve()
        .bodyToMono(BPSApprovalConfirmationResponse.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public List<ApprovalRequestType> getRequestTypes() {
    final String schemeCode = bpsProperties.getSchemeCode();
    return Stream.of(ApprovalRequestType.values())
        .filter(i -> i.getSupportedEnvironments().contains(schemeCode))
        .collect(toList());
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
