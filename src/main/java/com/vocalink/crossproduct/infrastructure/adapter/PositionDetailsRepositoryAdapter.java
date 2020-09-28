package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.domain.PositionDetailsRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.positions.CPPositionDetails;
import com.vocalink.crossproduct.shared.positions.PositionClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Slf4j
public class PositionDetailsRepositoryAdapter extends
    AbstractCrossproductAdapter<CPPositionDetails, PositionDetails> implements
    PositionDetailsRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<PositionDetails> findByParticipantId(String context, String participantId) {
    log.info("Fetching Position Details for participantID: {} from context {} ... ", participantId,
        context);
    PositionClient positionClient = clientFactory.getPositionClient(context);
    return positionClient.findByParticipantId(participantId)
        .stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }
}