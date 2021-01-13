package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSParticipantSettlement {

  private final String cycleId;
  private final ZonedDateTime settlementTime;
  private final String status;
  private final String participantId;

  @JsonCreator
  public BPSParticipantSettlement(
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "settlementTime") ZonedDateTime settlementTime,
      @JsonProperty(value = "status") String status,
      @JsonProperty(value = "participantId") String participantId) {
    this.cycleId = cycleId;
    this.settlementTime = settlementTime;
    this.status = status;
    this.participantId = participantId;
  }
}
