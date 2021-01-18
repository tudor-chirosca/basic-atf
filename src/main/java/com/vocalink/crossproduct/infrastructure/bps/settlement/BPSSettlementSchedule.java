package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSSettlementSchedule {

  private final List<BPSSettlementCycleSchedule> weekdayCycles;
  private final List<BPSSettlementCycleSchedule> weekendCycles;

  @JsonCreator
  public BPSSettlementSchedule(
      @JsonProperty("weekdayCycles") List<BPSSettlementCycleSchedule> weekdayCycles,
      @JsonProperty("weekendCycles") List<BPSSettlementCycleSchedule> weekendCycles) {
    this.weekdayCycles = weekdayCycles;
    this.weekendCycles = weekendCycles;
  }
}
