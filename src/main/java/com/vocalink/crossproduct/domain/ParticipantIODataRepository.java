package com.vocalink.crossproduct.domain;

import java.util.List;

public interface ParticipantIODataRepository {
  List<ParticipantIOData> findAll(String context);
}
