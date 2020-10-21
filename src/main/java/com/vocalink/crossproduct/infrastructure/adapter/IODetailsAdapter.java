package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.repository.IODetailsRepository;
import com.vocalink.crossproduct.shared.io.CPIODetails;
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
public class IODetailsAdapter extends
    AbstractCrossproductAdapter<CPIODetails, IODetails> implements IODetailsRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<IODetails> findIODetailsFor(String context, String participantId,
      LocalDate localDate) {
    log.info("Fetching IO Details for participantId {} from context {} ... ", participantId,
        context);
    ParticipantIODataClient client = clientFactory.getParticipantIODataClient(context);

    return client.findIODetailsFor(participantId, localDate)
        .stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}
