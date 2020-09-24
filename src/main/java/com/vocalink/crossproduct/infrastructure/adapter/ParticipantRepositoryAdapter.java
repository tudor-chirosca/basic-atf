package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.participant.CPParticipant;
import com.vocalink.crossproduct.shared.participant.ParticipantClient;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ParticipantRepositoryAdapter extends
    AbstractCrossproductAdapter<CPParticipant, Participant>
    implements ParticipantRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<Participant> findAll(String context) {
    log.info("Fetching all participants from context {} ... ", context);
    ParticipantClient participantClient = clientFactory.getParticipantClient(context.toUpperCase());

    return participantClient.findAll()
        .stream()
        .map(this::toEntity)
        .collect(toList());
  }

  @Override
  public Optional<Participant> findByParticipantId(String context, String participantId) {
    log.info("Fetching participant with id {} from context {} ... ", participantId, context);
    ParticipantClient participantClient = clientFactory.getParticipantClient(context.toUpperCase());

    return participantClient.findById(participantId)
        .stream()
        .map(this::toEntity)
        .findFirst();
  }
}
