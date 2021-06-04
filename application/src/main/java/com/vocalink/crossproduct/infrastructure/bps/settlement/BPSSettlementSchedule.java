package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

import lombok.Getter;

@Getter
public class BPSSettlementSchedule {

  private final ZonedDateTime updatedAt;
  private final List<BPSScheduleDayDetails> scheduleDayDetails;

  public BPSSettlementSchedule(
      @JsonProperty(value = "updatedAt") ZonedDateTime updatedAt,
      @JsonProperty(value = "scheduleDayDetails") List<BPSScheduleDayDetails> scheduleDayDetails) {
    this.updatedAt = updatedAt;
    this.scheduleDayDetails = scheduleDayDetails == null ? emptyList() : scheduleDayDetails;
  }
}
