package com.vocalink.crossproduct.infrastructure.bps.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class BPSIntraDayPositionGross {

  private final String schemeId;
  private final String debitParticipantId;
  private final LocalDate settlementDate;
  private final Amount debitCapAmount;
  private final Amount debitPositionAmount;

  @JsonCreator
  public BPSIntraDayPositionGross(
      @JsonProperty(value = "schemeId") String schemeId,
      @JsonProperty(value = "debitParticipantId") String debitParticipantId,
      @JsonProperty(value = "settlementDate") LocalDate settlementDate,
      @JsonProperty(value = "debitCapAmount") Amount debitCapAmount,
      @JsonProperty(value = "debitPositionAmount") Amount debitPositionAmount) {
    this.schemeId = schemeId;
    this.debitParticipantId = debitParticipantId;
    this.settlementDate = settlementDate;
    this.debitCapAmount = debitCapAmount;
    this.debitPositionAmount = debitPositionAmount;
  }
}
