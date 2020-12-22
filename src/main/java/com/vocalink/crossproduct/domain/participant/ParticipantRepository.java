package com.vocalink.crossproduct.domain.participant;

import java.util.List;

public interface ParticipantRepository {

  List<Participant> findAll(String context);

  List<Participant> findWith(String context, String connectingParty, String participantType);

  Participant findByParticipantId(String context, String participantId);
}
