package com.vocalink.crossproduct.domain.participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

  List<Participant> findAll(String context);

  Optional<Participant> findByParticipantId(String context, String participantId);
}
