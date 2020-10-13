package com.vocalink.crossproduct.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigInteger;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionDetailsTotalsDto {
  private BigInteger totalCredit;
  private BigInteger totalDebit;
  private BigInteger totalNetPosition;
}
