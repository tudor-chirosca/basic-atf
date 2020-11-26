package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.domain.io.IODetailsRepository;
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
public class IODetailsAdapter implements IODetailsRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<IODetails> findIODetailsFor(String context, String participantId,
      LocalDate localDate) {
    log.info("Fetching IO Details for participantId {} from context {} ... ", participantId,
        context);
    ParticipantIODataClient client = clientFactory.getParticipantIODataClient(context);

    return client.findIODetailsFor(participantId, localDate)
        .stream()
        .map(MAPPER::toEntity)
        .collect(Collectors.toList());
  }
}
