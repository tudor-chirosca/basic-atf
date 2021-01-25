package com.vocalink.crossproduct.ui.dto.position;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IntraDayPositionGrossDto {

  private final BigDecimal debitCap;
  private final BigDecimal debitPosition;
}
