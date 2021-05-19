package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class BPSIOBatchSummary {

  private final String messageType;
  private final Integer submitted;
  private final Integer accepted;
  private final String rejected;
  private final Integer output;

  public BPSIOBatchSummary(
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "submitted", required = true) Integer submitted,
      @JsonProperty(value = "accepted", required = true) Integer accepted,
      @JsonProperty(value = "rejected", required = true) String rejected,
      @JsonProperty(value = "output", required = true) Integer output) {
    this.messageType = messageType;
    this.submitted = submitted;
    this.accepted = accepted;
    this.rejected = rejected;
    this.output = output;
  }
}
