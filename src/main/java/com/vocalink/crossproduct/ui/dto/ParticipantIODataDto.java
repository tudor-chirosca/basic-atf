package com.vocalink.crossproduct.ui.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantIODataDto {
  private ParticipantDto participant;
  private IOData files;
  private IOData batches;
  private IOData transactions;
}
