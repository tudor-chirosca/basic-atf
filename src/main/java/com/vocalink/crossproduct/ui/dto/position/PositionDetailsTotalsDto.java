package com.vocalink.crossproduct.ui.dto.position;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionDetailsTotalsDto {
  private BigDecimal totalCredit;
  private BigDecimal totalDebit;
  private BigDecimal totalNetPosition;
}
