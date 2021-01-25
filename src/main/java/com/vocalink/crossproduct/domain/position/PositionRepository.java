package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.util.List;

public interface PositionRepository extends CrossproductRepository {

  List<ParticipantPosition> findByParticipantId(String schemeParticipantIdentifier);

}
