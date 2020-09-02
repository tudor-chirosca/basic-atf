package com.vocalink.crossproduct.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantIOData {
  private String participantId;
  private IOData files;
  private IOData batches;
  private IOData transactions;
}
