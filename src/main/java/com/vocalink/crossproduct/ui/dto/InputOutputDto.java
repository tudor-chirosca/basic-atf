package com.vocalink.crossproduct.ui.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputOutputDto {
  private String datetime;
  private double filesRejected;
  private double batchesRejected;
  private double transactionsRejected;
  private List<InputOutputItemDto> rows;

}
