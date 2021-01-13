package com.vocalink.crossproduct.infrastructure.bps.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Amount {

  private final BigDecimal amount;
  private final String currency;

  @JsonCreator
  public Amount(@JsonProperty(value = "amount") BigDecimal amount,
      @JsonProperty(value = "currency") String currency) {
    this.amount = amount;
    this.currency = currency;
  }
}
