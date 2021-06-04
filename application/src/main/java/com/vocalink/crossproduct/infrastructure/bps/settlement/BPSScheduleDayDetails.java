package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import static java.util.Collections.emptyList;

import lombok.Getter;

@Getter
public class BPSScheduleDayDetails {

  private final String weekDay;
  private final List<BPSSettlementCycleSchedule> cycles;

  public BPSScheduleDayDetails(
      @JsonProperty(value = "weekDay") String weekDay,
      @JsonProperty(value = "cycles") List<BPSSettlementCycleSchedule> cycles) {
    this.weekDay = weekDay;
    this.cycles = cycles == null ? emptyList() : cycles;
  }
}
