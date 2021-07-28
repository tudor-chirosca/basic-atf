package com.vocalink.crossproduct.domain.participant;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import java.util.Optional;

public interface ParticipantRepository extends CrossproductRepository {

  Page<Participant> findAll();

  Page<Participant> findByConnectingPartyAndType(String connectingParty, String participantType);

  Participant findById(String participantId);

  Optional<Participant> findByBic(String participantBic);

  Page<Participant> findPaginated(ManagedParticipantsSearchCriteria request);

  ParticipantConfiguration findConfigurationById(String participantId);
}
