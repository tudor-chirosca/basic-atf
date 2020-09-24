package com.vocalink.crossproduct.domain;

import java.util.List;

public interface CycleRepository {

  List<Cycle> findAll(String context);
  List<Cycle> findByIds(String context, List<String> cycleIds);
}
