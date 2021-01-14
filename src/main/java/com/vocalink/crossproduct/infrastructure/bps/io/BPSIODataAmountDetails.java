package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIODataAmountDetails {

  private final Integer submitted;
  private final Integer accepted;
  private final Integer output;
  private final Double rejected;
  private final Integer amountAccepted;
  private final Integer amountOutput;

  @JsonCreator
  public BPSIODataAmountDetails(
      @JsonProperty(value = "submitted") Integer submitted,
      @JsonProperty(value = "accepted") Integer accepted,
      @JsonProperty(value = "output") Integer output,
      @JsonProperty(value = "rejected") Double rejected,
      @JsonProperty(value = "amountAccepted") Integer amountAccepted,
      @JsonProperty(value = "amountOutput") Integer amountOutput) {
    this.submitted = submitted;
    this.rejected = rejected;
    this.accepted = accepted;
    this.output = output;
    this.amountAccepted = amountAccepted;
    this.amountOutput = amountOutput;
  }
}
