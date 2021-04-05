package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIOData {

  private final Integer submitted;
  private final String rejected;
  private final Integer output;

  @JsonCreator
  public BPSIOData(
      @JsonProperty(value = "submitted") Integer submitted,
      @JsonProperty(value = "rejected") String rejected,
      @JsonProperty(value = "output") Integer output) {
    this.submitted = submitted;
    this.rejected = rejected;
    this.output = output;
  }
}
