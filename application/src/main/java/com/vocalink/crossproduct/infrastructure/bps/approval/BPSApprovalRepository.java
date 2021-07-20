package com.vocalink.crossproduct.infrastructure.bps.approval;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.APPROVALS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.APPROVAL_DETAILS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.APPROVAL_REQUEST_BY_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.APPROVAL_REQUEST_TYPE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalRepository;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalCreationResponse;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSUserDetails;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSApprovalRepository implements ApprovalRepository {

  private static final String OFFSET = "offset";
  private static final String PAGE_SIZE = "pageSize";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Approval findByJobId(String approvalId) {
    final BPSApprovalDetailsRequest bpsRequest = new BPSApprovalDetailsRequest(approvalId);
    return webClient.post()
        .uri(resolve(APPROVAL_DETAILS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSApprovalDetailsRequest.class))
        .retrieve()
        .bodyToMono(BPSApproval.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public Page<Approval> findPaginated(ApprovalSearchCriteria criteria) {
    final BPSApprovalSearchRequest request = BPSMAPPER.toBps(criteria);
    final URI uri = UriComponentsBuilder.fromUri(resolve(APPROVALS_PATH, bpsProperties))
        .queryParam(OFFSET, criteria.getOffset())
        .queryParam(PAGE_SIZE, criteria.getLimit())
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSApprovalSearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSApproval>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(a -> MAPPER.toEntity(a, Approval.class))
        .block();
  }

  @Override
  public ApprovalCreationResponse requestApproval(ApprovalChangeCriteria criteria, String userId) {
    final BPSApprovalRequestType bpsApprovalRequestType = BPSMAPPER
        .toBpsApprovalRequestType(criteria.getRequestType());
    final Map<String, Object> approvalRequest = BPSMAPPER.approvalCreationRequest(criteria, userId);
    return webClient.put()
        .uri(resolve(bpsApprovalRequestType.getApprovalRequestPath(), bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(approvalRequest), Map.class))
        .retrieve()
        .bodyToMono(BPSApprovalCreationResponse.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public List<UserDetails> findRequestedDetails() {
    return webClient.post()
        .uri(resolve(APPROVAL_REQUEST_BY_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToFlux(BPSUserDetails.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public List<ApprovalRequestType> findApprovalRequestTypes() {
    return webClient.post()
        .uri(resolve(APPROVAL_REQUEST_TYPE_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToFlux(BPSApprovalRequestType.class)
        .map(a -> MAPPER.convertApprovalRequestType(a.name()))
        .retryWhen(retryWebClientConfig.fixedRetry())
        .collectList()
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
