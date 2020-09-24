package com.vocalink.crossproduct.domain;

import java.util.List;

public interface PositionDetailsRepository {

  List<PositionDetails> findByParticipantId(String context, String participantId);
}
