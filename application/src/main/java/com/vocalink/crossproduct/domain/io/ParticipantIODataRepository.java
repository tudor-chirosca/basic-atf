package com.vocalink.crossproduct.domain.io;

import com.vocalink.crossproduct.domain.CrossproductRepository;

public interface ParticipantIODataRepository extends CrossproductRepository {

  IODetails findByParticipantId(String participantId);

  IODashboard findAll();
}
