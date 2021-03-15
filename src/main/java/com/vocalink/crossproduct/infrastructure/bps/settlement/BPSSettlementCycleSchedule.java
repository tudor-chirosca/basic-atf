package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSSettlementCycleSchedule {

  private final String sessionCode;
  private final String startTime;
  private final String endTime;
  private final String settlementTime;

  public BPSSettlementCycleSchedule(
      @JsonProperty(value = "sessionCode") String sessionCode,
      @JsonProperty(value = "startTime") String startTime,
      @JsonProperty(value = "endTime") String endTime,
      @JsonProperty(value = "settlementTime") String settlementTime) {
    this.sessionCode = sessionCode;
    this.startTime = startTime;
    this.endTime = endTime;
    this.settlementTime = settlementTime;
  }
}
