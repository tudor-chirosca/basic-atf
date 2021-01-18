package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSSettlementCycleSchedule {

  private final String cycleName;
  private final String startTime;
  private final String cutOffTime;
  private final String settlementStartTime;

  @JsonCreator
  public BPSSettlementCycleSchedule(
      @JsonProperty("cycleName") String cycleName,
      @JsonProperty("startTime") String startTime,
      @JsonProperty("cutOffTime") String cutOffTime,
      @JsonProperty("settlementStartTime") String settlementStartTime) {
    this.cycleName = cycleName;
    this.startTime = startTime;
    this.cutOffTime = cutOffTime;
    this.settlementStartTime = settlementStartTime;
  }
}
