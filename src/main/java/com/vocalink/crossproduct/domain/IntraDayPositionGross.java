package com.vocalink.crossproduct.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntraDayPositionGross {

  private BigDecimal debitCap;
  private BigDecimal debitPosition;

}
