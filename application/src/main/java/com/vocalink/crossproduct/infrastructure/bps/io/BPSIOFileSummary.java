package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIOFileSummary {

  private final Integer submitted;
  private final Integer accepted;
  private final String rejected;
  private final Integer output;

  @JsonCreator
  public BPSIOFileSummary(
      @JsonProperty(value = "submitted") Integer submitted,
      @JsonProperty(value = "accepted") Integer accepted,
      @JsonProperty(value = "output") Integer output,
      @JsonProperty(value = "rejected") String rejected) {
    this.submitted = submitted;
    this.rejected = rejected;
    this.output = output;
    this.accepted = accepted;
  }
}
