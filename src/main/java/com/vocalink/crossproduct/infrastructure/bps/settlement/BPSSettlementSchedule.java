package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSSettlementSchedule {

  private final String weekDay;
  private final List<BPSSettlementCycleSchedule> cycles;

  public BPSSettlementSchedule(
      @JsonProperty(value = "weekDay") String weekDay,
      @JsonProperty(value = "cycles") List<BPSSettlementCycleSchedule> cycles) {
    this.weekDay = weekDay;
    this.cycles = cycles;
  }
}
