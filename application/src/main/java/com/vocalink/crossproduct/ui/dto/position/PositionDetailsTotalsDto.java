package com.vocalink.crossproduct.ui.dto.position;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class PositionDetailsTotalsDto {

  private final BigDecimal totalCredit;
  private final BigDecimal totalDebit;
  @Setter
  @JsonInclude(Include.NON_EMPTY)
  private BigDecimal totalNetPosition;
}
