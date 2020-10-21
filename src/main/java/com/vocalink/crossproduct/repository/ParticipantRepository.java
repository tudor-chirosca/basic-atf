package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.participant.Participant;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

  List<Participant> findAll(String context);

  Optional<Participant> findByParticipantId(String context, String participantId);
}
