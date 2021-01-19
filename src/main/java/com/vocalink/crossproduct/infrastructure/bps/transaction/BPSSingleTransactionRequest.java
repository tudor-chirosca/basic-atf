package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSSingleTransactionRequest {

  private final String instructionId;

  @JsonCreator
  public BPSSingleTransactionRequest(
      @JsonProperty(value = "instructionId") String instructionId) {
    this.instructionId = instructionId;
  }
}
