package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum BPSEnquiryType {
  @JsonProperty("FILE")
  FILES("FILE"),
  @JsonProperty("BATCH")
  BATCHES("BATCH"),
  @JsonProperty("TRANSACTION")
  TRANSACTIONS("TRANSACTION");

  private final String description;

  BPSEnquiryType(String description) {
    this.description = description;
  }
}
