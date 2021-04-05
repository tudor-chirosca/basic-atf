package com.vocalink.crossproduct.domain.io;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.time.LocalDate;
import java.util.List;

public interface ParticipantIODataRepository extends CrossproductRepository {

  IODetails findByParticipantId(String participantId);

  IODashboard findAll();
}
