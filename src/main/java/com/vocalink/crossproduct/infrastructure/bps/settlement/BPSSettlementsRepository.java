package com.vocalink.crossproduct.infrastructure.bps.settlement;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_DETAILS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_SCHEDULE_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementDetails;
import com.vocalink.crossproduct.domain.settlement.SettlementDetailsSearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementsRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BPSSettlementsRepository implements SettlementsRepository {

  private static final String OFFSET = "offset";
  private static final String PAGE_SIZE = "pageSize";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Page<SettlementDetails> findDetails(SettlementDetailsSearchCriteria criteria) {
    final BPSSettlementDetailsRequest bpsRequest = BPSMAPPER.toBps(criteria);
    final URI uri = UriComponentsBuilder.fromUri(resolve(SETTLEMENT_DETAILS_PATH, bpsProperties))
        .queryParam(OFFSET, criteria.getOffset())
        .queryParam(PAGE_SIZE, criteria.getLimit())
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSettlementDetailsRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSResult<BPSSettlementDetails>> () {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(x -> MAPPER.toEntity(x, SettlementDetails.class))
        .block();
  }

  @Override
  public Page<ParticipantSettlement> findPaginated(SettlementEnquirySearchCriteria request) {
    final BPSSettlementEnquiryRequest bpsRequest = BPSMAPPER.toBps(request);
    final URI uri = UriComponentsBuilder.fromUri(resolve(SETTLEMENT_ENQUIRIES_PATH, bpsProperties))
        .queryParam(OFFSET, request.getOffset())
        .queryParam(PAGE_SIZE, request.getLimit())
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSettlementEnquiryRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSResult<BPSParticipantSettlement>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(ps -> MAPPER.toEntity(ps, ParticipantSettlement.class))
        .block();
  }

  @Override
  public SettlementSchedule findSchedule() {
    return webClient.post()
        .uri(resolve(SETTLEMENT_SCHEDULE_ENQUIRIES_PATH, bpsProperties))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(BPSSettlementSchedule.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
