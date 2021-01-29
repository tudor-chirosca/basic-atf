package com.vocalink.crossproduct.domain.participant;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.List;

public interface ParticipantRepository extends CrossproductRepository {

  List<Participant> findAll();

  List<Participant> findByConnectingPartyAndType(String connectingParty, String participantType);

  Participant findById(String participantId);

  Page<Participant> findPaginated(ManagedParticipantsSearchCriteria request);
}
