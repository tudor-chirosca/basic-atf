package com.vocalink.crossproduct.infrastructure.bps.alert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSAlertStatsData {

  private final String priority;
  private final int count;

  @JsonCreator
  public BPSAlertStatsData(
      @JsonProperty(value = "priority") String priority,
      @JsonProperty(value = "count") int count) {
    this.priority = priority;
    this.count = count;
  }
}
