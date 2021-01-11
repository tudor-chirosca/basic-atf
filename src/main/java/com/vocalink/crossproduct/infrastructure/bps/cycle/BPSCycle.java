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
  public BPSCycle(@JsonProperty("cycleId") String cycleId,
      @JsonProperty("status") String status,
      @JsonProperty("settlementTime") ZonedDateTime settlementTime,
      @JsonProperty("fileSubmissionCutOffTime") ZonedDateTime fileSubmissionCutOffTime,
      @JsonProperty("isNextDayCycle") Boolean isNextDayCycle,
      @JsonProperty("settlementConfirmationTime") ZonedDateTime settlementConfirmationTime) {
    this.cycleId = cycleId;
    this.status = status;
    this.settlementTime = settlementTime;
    this.fileSubmissionCutOffTime = fileSubmissionCutOffTime;
    this.isNextDayCycle = isNextDayCycle;
    this.settlementConfirmationTime = settlementConfirmationTime;
  }
}
