package com.vocalink.crossproduct.domain.cycle;

import java.time.LocalDate;
import java.util.List;

public interface CycleRepository {

  List<Cycle> findAll(String context);
  List<Cycle> findByIds(String context, List<String> cycleIds);
  List<Cycle> findCyclesByDate(String context, LocalDate date);
}
