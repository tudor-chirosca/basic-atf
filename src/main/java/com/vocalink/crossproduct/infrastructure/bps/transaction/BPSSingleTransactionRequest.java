package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSSingleTransactionRequest {

  private final String txnsInstructionId;

  @JsonCreator
  public BPSSingleTransactionRequest(
      @JsonProperty(value = "txnsInstructionId") String txnsInstructionId) {
    this.txnsInstructionId = txnsInstructionId;
  }
}
