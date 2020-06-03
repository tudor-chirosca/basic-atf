package com.vocalink.portal.infrastructure;

import com.vocalink.portal.domain.Cycle;
import com.vocalink.portal.domain.CycleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ApiCycleRepository implements CycleRepository {

  private final ApiClient apiClient;

  @Override
  public Mono<Cycle[]> fetchCycles() {
    log.info("Fetch cycles..");
    return apiClient.fetchCycles();
  }
}
