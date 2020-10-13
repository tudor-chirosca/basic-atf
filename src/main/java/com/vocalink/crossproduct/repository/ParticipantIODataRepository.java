package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.ParticipantIOData;
import java.time.LocalDate;
import java.util.List;

public interface ParticipantIODataRepository {

  List<ParticipantIOData> findByTimestamp(String context, LocalDate dateFrom);
}
