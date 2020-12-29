package com.vocalink.crossproduct.domain.participant;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository {

  List<Participant> findAll(String context);

  Optional<Participant> findBy(String context, String participantId);

  List<Participant> findBy(String context, String connectingParty, String participantType);

}
