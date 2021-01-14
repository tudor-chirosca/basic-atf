package com.vocalink.crossproduct.domain.io;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.time.LocalDate;
import java.util.List;

public interface ParticipantIODataRepository extends CrossproductRepository {

  List<ParticipantIOData> findByTimestamp(LocalDate dateFrom);

  List<IODetails> findIODetailsFor(String participantId, LocalDate date);
}
