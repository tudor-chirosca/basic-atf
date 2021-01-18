package com.vocalink.crossproduct.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Amount {

  private final BigDecimal amount;
  private final String currency;
}
