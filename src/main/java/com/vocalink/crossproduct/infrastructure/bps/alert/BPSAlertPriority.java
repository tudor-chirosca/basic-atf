package com.vocalink.crossproduct.infrastructure.bps.alert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSAlertPriority {

  private final String name;
  private final int threshold;
  private final Boolean highlight;

  @JsonCreator
  public BPSAlertPriority(
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "threshold") int threshold,
      @JsonProperty(value = "highlight") Boolean highlight) {
    this.name = name;
    this.threshold = threshold;
    this.highlight = highlight;
  }
}
