package com.vocalink.crossproduct.domain;

import java.time.LocalDate;
import java.util.List;

public interface ParticipantIODataRepository {

  List<ParticipantIOData> findByTimestamp(String context, LocalDate dateFrom);
}
