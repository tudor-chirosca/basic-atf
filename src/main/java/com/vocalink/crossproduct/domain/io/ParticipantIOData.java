package com.vocalink.crossproduct.domain.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantIOData {

  private final String participantId;
  private final IOData files;
  private final IOData batches;
  private final IOData transactions;
}
