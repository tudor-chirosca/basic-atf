package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantRepository;
import com.vocalink.crossproduct.domain.ParticipantStatus;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.participant.ParticipantClient;
import com.vocalink.crossproduct.shared.participant.ParticipantDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class ParticipantRepositoryAdapter implements ParticipantRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<Participant> findAll(String context) {
    log.info("Fetching all participants from context {} ... ", context);

    ParticipantClient participantClient = clientFactory.getParticipantClient(context.toUpperCase());

    return participantClient.findAll()
        .stream()
        .map(this::toEntity)
        .collect(Collectors.toList());
  }

  private Participant toEntity(ParticipantDto participantDto){
    return Participant.builder()
        .id(participantDto.getId())
        .bic(participantDto.getBic())
        .name(participantDto.getName())
        .suspendedTime(!StringUtils.isBlank(participantDto.getSuspendedTime()) ?
            LocalDateTime.parse(participantDto.getSuspendedTime()): null)
        .status(ParticipantStatus.valueOf(participantDto.getStatus()))
        .build();
  }
}
