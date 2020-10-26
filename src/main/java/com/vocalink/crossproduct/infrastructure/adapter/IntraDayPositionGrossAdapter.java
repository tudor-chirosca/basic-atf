package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.repository.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.shared.positions.PositionClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class IntraDayPositionGrossAdapter implements
    IntraDayPositionGrossRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<IntraDayPositionGross> findIntraDayPositionGrossByParticipantId(String context,
      List<String> participantId) {
    log.info("Fetching Intra-day position Gross for participantID: {} from context {} ... ",
        participantId, context);
    PositionClient client = clientFactory.getPositionClient(context);

    return client
        .findIntraDayPositionsGrossByParticipantId(participantId)
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }
}
