package com.vocalink.crossproduct.domain.io;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IODataAmountDetails {

  private final Integer submitted;
  private final Integer accepted;
  private final Integer output;
  private final Double rejected;
  private final BigDecimal amountAccepted;
  private final BigDecimal amountOutput;
}
