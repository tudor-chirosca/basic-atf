package com.vocalink.crossproduct.domain.position;

import java.util.List;

public interface PositionDetailsRepository {

  List<PositionDetails> findByParticipantId(String context, String participantId);

}
