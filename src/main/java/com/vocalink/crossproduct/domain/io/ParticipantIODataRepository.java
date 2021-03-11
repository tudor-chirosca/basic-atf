package com.vocalink.crossproduct.domain.io;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.time.LocalDate;
import java.util.List;

public interface ParticipantIODataRepository extends CrossproductRepository {

  List<ParticipantIOData> findByTimestamp(LocalDate dateFrom);

  IODetails findByParticipantId(String participantId);
}
