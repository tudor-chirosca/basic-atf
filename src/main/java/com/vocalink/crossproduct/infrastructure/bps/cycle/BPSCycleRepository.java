package com.vocalink.crossproduct.infrastructure.bps.cycle;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants.SCHEME_CODE;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.CYCLES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_POSITION_PATH;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSPositionsRequest;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class BPSCycleRepository implements CycleRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<Cycle> findAll() {
    BPSCycleRequest request = new BPSCycleRequest(SCHEME_CODE);
    List<BPSCycle> bpsCycles = getCycles(request).getCycles();

    final List<String> cycleIds = bpsCycles.stream()
        .map(BPSCycle::getCycleId).collect(toList());

    final List<BPSSettlementPosition> positions = getSettlementPositions()
        .getMlSettlementPositions().stream()
        .filter(p -> cycleIds.contains(p.getCycleId()))
        .collect(toList());

    return collectCycles(bpsCycles, positions);
  }

  @Override
  public List<Cycle> findByIds(List<String> cycleIds) {
    BPSCycleRequest request = new BPSCycleRequest(SCHEME_CODE);

    return getCycles(request).getCycles().stream()
        .filter(c -> cycleIds.contains(c.getCycleId()))
        .map(BPSMAPPER::toEntity)
        .collect(toList());
  }

  private List<Cycle> collectCycles(List<BPSCycle> bpsCycles,
      List<BPSSettlementPosition> positions) {
    return bpsCycles.stream()
        .map(bpsCycle ->
            Cycle.builder()
                .id(bpsCycle.getCycleId())
                .settlementTime(bpsCycle.getSettlementTime())
                .cutOffTime(bpsCycle.getFileSubmissionCutOffTime())
                .settlementConfirmationTime(bpsCycle.getSettlementConfirmationTime())
                .status(CycleStatus.valueOf(bpsCycle.getStatus()))
                .isNextDayCycle(bpsCycle.getIsNextDayCycle())
                .totalPositions(positions.stream()
                    .filter(pos -> pos.getCycleId().equals(bpsCycle.getCycleId()))
                    .map(MAPPER::toEntity)
                    .collect(toList()))
                .build())
        .collect(toList());
  }

  @Override
  public List<Cycle> findByDate(LocalDate date) {
    BPSCycleRequest request = new BPSCycleRequest(SCHEME_CODE);

    return getCycles(request).getCycles().stream()
        .filter(c -> date.equals(c.getFileSubmissionCutOffTime().toLocalDate()))
        .map(BPSMAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public List<Cycle> findLatest(int nrLatestCycles) {
    BPSCycleRequest request = new BPSCycleRequest(SCHEME_CODE);
    request.setNumberOfCycles(nrLatestCycles);

    return getCycles(request).getCycles().stream()
        .map(BPSMAPPER::toEntity)
        .collect(toList());
  }

  private BPSCycleWrapper getCycles(BPSCycleRequest request) {
    return webClient.post()
        .uri(resolve(CYCLES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSCycleRequest.class))
        .retrieve()
        .bodyToMono(BPSCycleWrapper.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .block();
  }

  private BPSSettlementPositionWrapper getSettlementPositions() {
    final BPSPositionsRequest request = BPSPositionsRequest.builder()
        .schemeCode(SCHEME_CODE)
        .build();

    return webClient.post()
        .uri(resolve(SETTLEMENT_POSITION_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSPositionsRequest.class))
        .retrieve()
        .bodyToMono(BPSSettlementPositionWrapper.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
