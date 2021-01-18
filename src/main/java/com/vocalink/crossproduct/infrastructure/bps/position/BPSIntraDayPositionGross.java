package com.vocalink.crossproduct.infrastructure.bps.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class BPSIntraDayPositionGross {

  private final String schemeId;
  private final String debitParticipantId;
  private final LocalDate settlementDate;
  private final BPSAmount debitCapAmount;
  private final BPSAmount debitPositionAmount;

  @JsonCreator
  public BPSIntraDayPositionGross(
      @JsonProperty(value = "schemeId") String schemeId,
      @JsonProperty(value = "debitParticipantId") String debitParticipantId,
      @JsonProperty(value = "settlementDate") LocalDate settlementDate,
      @JsonProperty(value = "debitCapAmount") BPSAmount debitCapAmount,
      @JsonProperty(value = "debitPositionAmount") BPSAmount debitPositionAmount) {
    this.schemeId = schemeId;
    this.debitParticipantId = debitParticipantId;
    this.settlementDate = settlementDate;
    this.debitCapAmount = debitCapAmount;
    this.debitPositionAmount = debitPositionAmount;
  }
}
