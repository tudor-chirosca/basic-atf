package com.vocalink.crossproduct.ui.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputOutputItemDto {
  private ParticipantDto participant;
  private InputOutputDataDto files;
  private InputOutputDataDto batches;
  private InputOutputDataDto transactions;
}
