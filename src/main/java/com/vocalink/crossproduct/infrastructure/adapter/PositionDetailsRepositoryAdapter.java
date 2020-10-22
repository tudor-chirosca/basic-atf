package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.repository.PositionDetailsRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.positions.PositionClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PositionDetailsRepositoryAdapter implements PositionDetailsRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<PositionDetails> findByParticipantId(String context, String participantId) {
    log.info("Fetching Position Details for participantID: {} from context {} ... ", participantId,
        context);
    PositionClient client = clientFactory.getPositionClient(context);

    return client.findByParticipantId(participantId)
        .stream()
        .map(MAPPER::toEntity)
        .collect(Collectors.toList());
  }
}