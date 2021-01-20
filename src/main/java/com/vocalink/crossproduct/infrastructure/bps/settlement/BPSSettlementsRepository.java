package com.vocalink.crossproduct.infrastructure.bps.settlement;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.INSTRUCTION_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_SCHEDULE_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SINGLE_SETTLEMENT_PATH;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.settlement.BPSInstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.BPSSettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementsRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BPSSettlementsRepository implements SettlementsRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public ParticipantSettlement findBy(BPSInstructionEnquirySearchCriteria criteria) {
    final BPSInstructionEnquiryRequest request = BPSMAPPER.toBps(criteria);
    final BPSParticipantSettlement settlement = findSettlement(request.getCycleId(),
        request.getParticipantId());
    final BPSPage<BPSParticipantInstruction> instructions = findInstructions(request);
    return MAPPER.toEntity(settlement, instructions);
  }

  private BPSParticipantSettlement findSettlement(String cycleId, String participantId) {
    final BPSSingleSettlementRequest request = new BPSSingleSettlementRequest(cycleId,
        participantId);

    return webClient.post()
        .uri(resolve(SINGLE_SETTLEMENT_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSSingleSettlementRequest.class))
        .retrieve()
        .bodyToMono(BPSParticipantSettlement.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .block();
  }

  private BPSPage<BPSParticipantInstruction> findInstructions(
      BPSInstructionEnquiryRequest request) {
    return webClient.post()
        .uri(resolve(INSTRUCTION_ENQUIRIES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSInstructionEnquiryRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSParticipantInstruction>>() {
        })
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .block();
  }

  @Override
  public Page<ParticipantSettlement> findBy(BPSSettlementEnquirySearchCriteria criteria) {
    final BPSSettlementEnquiryRequest request = BPSMAPPER.toBps(criteria);
    return webClient.post()
        .uri(resolve(SETTLEMENT_ENQUIRIES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSSettlementEnquiryRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSParticipantSettlement>>() {
        })
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public SettlementSchedule findSchedule() {
    return webClient.post()
        .uri(resolve(SETTLEMENT_SCHEDULE_ENQUIRIES_PATH, bpsProperties))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just("{}"), String.class))
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
