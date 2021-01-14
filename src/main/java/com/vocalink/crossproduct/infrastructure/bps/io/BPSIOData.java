package com.vocalink.crossproduct.infrastructure.bps.io;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIOData {

  private final Integer submitted;
  private final Double rejected;

  @JsonCreator
  public BPSIOData(
      @JsonProperty(value = "submitted") Integer submitted,
      @JsonProperty(value = "rejected") Double rejected) {
    this.submitted = submitted;
    this.rejected = rejected;
  }
}
