package com.vocalink.crossproduct.infrastructure;

import static java.util.Arrays.asList;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.CycleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ApiCycleRepository implements CycleRepository {

  private final ApiClient apiClient;

  @Override
  public List<Cycle> fetchCycles() {
    log.info("Fetch cycles..");
    return asList(apiClient.fetchCycles().block());
  }
}
