package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.repository.CycleRepository;
import com.vocalink.crossproduct.shared.cycle.CyclesClient;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class CycleRepositoryAdapter implements CycleRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<Cycle> findAll(String context) {
    log.info("Fetching all cycles from context {} ... ", context);
    CyclesClient client = clientFactory.getCyclesClient(context);

    return client.findAll()
        .stream()
        .map(MAPPER::toEntity)
        .collect(Collectors.toList());
  }

  @Override
  public List<Cycle> findByIds(String context, List<String> cycleIds) {
    log.info("Fetching cycle by cycleIds {} from context {} ... ", cycleIds, context);
    CyclesClient client = clientFactory.getCyclesClient(context);

    return client.findByIds(cycleIds)
        .stream()
        .map(MAPPER::toEntity)
        .collect(Collectors.toList());
  }
}
