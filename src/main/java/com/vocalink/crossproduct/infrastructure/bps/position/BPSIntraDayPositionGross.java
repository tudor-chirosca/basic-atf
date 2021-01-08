package com.vocalink.crossproduct.infrastructure.bps.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class BPSIntraDayPositionGross {

  private final String participantId;
  private final BigDecimal debitCap;
  private final BigDecimal debitPosition;

  @JsonCreator
  public BPSIntraDayPositionGross(@JsonProperty(value = "participantId") String participantId,
                                  @JsonProperty(value = "debitCap") BigDecimal debitCap,
                                  @JsonProperty(value = "debitPosition") BigDecimal debitPosition) {
    this.participantId = participantId;
    this.debitCap = debitCap;
    this.debitPosition = debitPosition;
  }
}
