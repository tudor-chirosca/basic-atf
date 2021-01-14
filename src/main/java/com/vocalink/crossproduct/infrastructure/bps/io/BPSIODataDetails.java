package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIODataDetails {

  private final Integer submitted;
  private final Integer accepted;
  private final Integer output;
  private final Double rejected;

  @JsonCreator
  public BPSIODataDetails(
      @JsonProperty(value = "submitted") Integer submitted,
      @JsonProperty(value = "accepted") Integer accepted,
      @JsonProperty(value = "output") Integer output,
      @JsonProperty(value = "rejected") Double rejected) {
    this.submitted = submitted;
    this.rejected = rejected;
    this.accepted = accepted;
    this.output = output;
  }
}
