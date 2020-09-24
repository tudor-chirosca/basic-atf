package com.vocalink.crossproduct.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PositionDetailsTotalsDto {
  private BigInteger totalCredit;
  private BigInteger totalDebit;
  private BigInteger totalNetPosition;
}
