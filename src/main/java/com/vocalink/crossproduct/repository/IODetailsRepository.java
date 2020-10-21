package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.io.IODetails;
import java.time.LocalDate;
import java.util.List;

public interface IODetailsRepository {

  List<IODetails> findIODetailsFor(String context, String participantId, LocalDate localDate);
}
