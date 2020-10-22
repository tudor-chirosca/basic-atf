package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.repository.ParticipantIODataRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ParticipantIODataAdapter implements ParticipantIODataRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<ParticipantIOData> findByTimestamp(String context, LocalDate dateFrom) {
    log.info("Fetching participant IO Data from context {} ... ", context);
    ParticipantIODataClient client = clientFactory.getParticipantIODataClient(context);

    return client.findByTimestamp(dateFrom)
        .stream()
        .map(MAPPER::toEntity)
        .collect(Collectors.toList());
  }
}
