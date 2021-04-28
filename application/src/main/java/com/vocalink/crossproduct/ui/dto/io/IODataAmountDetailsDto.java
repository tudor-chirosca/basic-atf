package com.vocalink.crossproduct.ui.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IODataAmountDetailsDto {

  private final Integer submitted;
  private final Integer accepted;
  private final Integer output;
  private final Double rejected;
  private final Integer amountAccepted;
  private final Integer amountOutput;
}
