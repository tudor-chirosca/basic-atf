package com.vocalink.crossproduct.domain.participant;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;

public interface ParticipantRepository extends CrossproductRepository {

  List<Participant> findAll();

  List<Participant> findWith(String connectingParty, String participantType);

  Participant findById(String participantId);
}
