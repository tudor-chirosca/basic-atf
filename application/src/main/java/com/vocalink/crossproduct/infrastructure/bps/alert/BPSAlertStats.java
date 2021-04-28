package com.vocalink.crossproduct.infrastructure.bps.alert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSAlertStats {

  private final int total;
  private final List<BPSAlertStatsData> items;

  @JsonCreator
  public BPSAlertStats(
      @JsonProperty(value = "total") int total,
      @JsonProperty(value = "items") List<BPSAlertStatsData> items) {
    this.total = total;
    this.items = items;
  }
}
