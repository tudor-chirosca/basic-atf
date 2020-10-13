package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.PositionDetails;
import java.util.List;

public interface PositionDetailsRepository {

  List<PositionDetails> findByParticipantId(String context, String participantId);

}
