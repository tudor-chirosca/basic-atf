package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.participant.CPParticipantsSearchRequest;
import com.vocalink.crossproduct.shared.participant.ParticipantClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ParticipantsAdapter implements ParticipantRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<Participant> findAll(String context) {
    log.info("Fetching all participants from context {} ... ", context);
    ParticipantClient client = clientFactory.getParticipantClient(context.toUpperCase());

    return client.findAll()
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public List<Participant> findWith(String context, String connectingParty, String participantType) {
    log.info("Fetching participants from context {} for connectingParty: {} "
        + "and participantType: {} ... ", context, connectingParty, participantType);
    ParticipantClient client = clientFactory.getParticipantClient(context.toUpperCase());

    CPParticipantsSearchRequest request = MAPPER.toCp(connectingParty, participantType);

    return client.findWith(request)
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }

  @Override
  public Participant findByParticipantId(String context, String participantId) {
    log.info("Fetching participant with id {} from context {} ... ", participantId, context);
    ParticipantClient client = clientFactory.getParticipantClient(context.toUpperCase());

    return MAPPER.toEntity(client.findById(participantId));
  }
}
