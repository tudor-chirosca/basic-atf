package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSCycle {
  private final String cycleId;
  private final String status;
  private final ZonedDateTime settlementTime;
  private final ZonedDateTime fileSubmissionCutOffTime;
  private final Boolean isNextDayCycle;
  private final ZonedDateTime settlementConfirmationTime;

  @JsonCreator
  public BPSCycle(
      @JsonProperty(value = "cycleId", required = true) String cycleId,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "settlementTime", required = true) ZonedDateTime settlementTime,
      @JsonProperty(value = "fileSubmissionCutOffTime", required = true) ZonedDateTime fileSubmissionCutOffTime,
      @JsonProperty(value = "isNextDayCycle",required = true) Boolean isNextDayCycle,
      @JsonProperty(value = "settlementConfirmationTime") ZonedDateTime settlementConfirmationTime) {
    this.cycleId = cycleId;
    this.status = status;
    this.settlementTime = settlementTime;
    this.fileSubmissionCutOffTime = fileSubmissionCutOffTime;
    this.isNextDayCycle = isNextDayCycle;
    this.settlementConfirmationTime = settlementConfirmationTime;
  }
}
