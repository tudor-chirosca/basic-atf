package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycleStatus;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSParticipantSettlement {

  private final String cycleId;
  private final ZonedDateTime settlementStartDate;
  private final BPSCycleStatus status;
  private final String schemeParticipantIdentifier;

  @JsonCreator
  public BPSParticipantSettlement(
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "settlementStartDate") ZonedDateTime settlementStartDate,
      @JsonProperty(value = "status") BPSCycleStatus status,
      @JsonProperty(value = "schemeParticipantIdentifier") String schemeParticipantIdentifier) {
    this.cycleId = cycleId;
    this.settlementStartDate = settlementStartDate;
    this.status = status;
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
  }
}
