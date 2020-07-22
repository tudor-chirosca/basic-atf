package com.vocalink.portal.domain;

import java.util.List;

public interface ParticipantRepository {

  List<Participant> fetchParticipants();
}
