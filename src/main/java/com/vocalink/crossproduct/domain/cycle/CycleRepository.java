package com.vocalink.crossproduct.domain.cycle;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import java.time.LocalDate;
import java.util.List;

public interface CycleRepository extends CrossproductRepository {

  List<Cycle> findAll();

  List<Cycle> findByIds(List<String> cycleIds);

  List<DayCycle> findByDate(LocalDate date);

  List<Cycle> findLatest(int nrLatestCycles);
}
