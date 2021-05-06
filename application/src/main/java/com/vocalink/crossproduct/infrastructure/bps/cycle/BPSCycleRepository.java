package com.vocalink.crossproduct.infrastructure.bps.cycle;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.CYCLES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.DAY_CYCLES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_POSITION_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSPositionsRequest;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class BPSCycleRepository implements CycleRepository {

  private static final String CYCLE_ID_DATE_FORMAT = "yyyyMMdd";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<Cycle> findAll() {
    final BPSCycleRequest request = new BPSCycleRequest(bpsProperties.getSchemeCode());
    final List<BPSCycle> bpsCycles = getCycles(request).getCycles();

    final List<String> cycleIds = bpsCycles.stream()
        .map(BPSCycle::getCycleId).collect(toList());

    final DateTimeFormatter cycleIdDateFormatter = DateTimeFormatter.ofPattern(CYCLE_ID_DATE_FORMAT);
    final List<BPSSettlementPosition> positions = getSettlementPositions()
        .getMlSettlementPositions().stream()
        .filter(p -> cycleIds.contains(p.getSettlementDate().format(cycleIdDateFormatter) + p.getCycleId()))
        .collect(toList());

    Map<String, List<BPSSettlementPosition>> settlementPositionMap = new HashMap<>();
    cycleIds.forEach(e -> settlementPositionMap.put(e, new ArrayList<>()));

    positions.forEach(e -> {
      String cycleId = e.getSettlementDate().format(cycleIdDateFormatter) + e.getCycleId();
      if (settlementPositionMap.containsKey(cycleId)) {
        settlementPositionMap.get(cycleId).add(e);
      }
    });

    return bpsCycles.stream()
        .map(c -> MAPPER.toEntity(c, settlementPositionMap))
        .collect(toList());
  }

  @Override
  public List<Cycle> findByIds(List<String> cycleIds) {
    final BPSCycleRequest request = new BPSCycleRequest(bpsProperties.getSchemeCode());

    return getCycles(request).getCycles().stream()
        .filter(c -> cycleIds.contains(c.getCycleId()))
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public List<DayCycle> findByDate(LocalDate date) {
    final BPSDayCycleRequest request = new BPSDayCycleRequest(bpsProperties.getSchemeCode(), date);
    return webClient.post()
        .uri(resolve(DAY_CYCLES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSDayCycleRequest.class))
        .retrieve()
        .bodyToFlux(BPSDayCycle.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public List<Cycle> findLatest(int nrLatestCycles) {
    BPSCycleRequest request = new BPSCycleRequest(bpsProperties.getSchemeCode());
    request.setNumberOfCycles(nrLatestCycles);

    return getCycles(request).getCycles().stream()
        .map(MAPPER::toEntity)
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
        .schemeCode(bpsProperties.getSchemeCode())
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
