package com.vocalink.crossproduct.ui.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantIODataDto {
  private ParticipantDto participant;
  private IODataDto files;
  private IODataDto batches;
  private IODataDto transactions;
}
