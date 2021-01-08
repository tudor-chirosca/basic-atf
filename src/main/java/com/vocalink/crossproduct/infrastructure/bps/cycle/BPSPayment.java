package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSPayment {

  private final Long count;
  private final BPSAmount amount;

  @JsonCreator
  public BPSPayment(@JsonProperty(value = "count") Long count,
      @JsonProperty(value = "amount") BPSAmount amount) {
    this.count = count;
    this.amount = amount;
  }
}
