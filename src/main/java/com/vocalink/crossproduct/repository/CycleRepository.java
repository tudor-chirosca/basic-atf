package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.Cycle;
import java.util.List;

public interface CycleRepository {

  List<Cycle> findAll(String context);
  List<Cycle> findByIds(String context, List<String> cycleIds);
}
