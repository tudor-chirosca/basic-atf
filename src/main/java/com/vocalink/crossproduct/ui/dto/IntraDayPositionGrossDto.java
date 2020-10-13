package com.vocalink.crossproduct.ui.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntraDayPositionGrossDto {

  private BigDecimal debitCap;
  private BigDecimal debitPosition;
}
