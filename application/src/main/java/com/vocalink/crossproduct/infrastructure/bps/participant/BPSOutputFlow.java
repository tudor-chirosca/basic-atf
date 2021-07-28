package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSOutputFlow {

  private final String messageType;
  private final Integer txnVolume;
  private final Integer txnTimeLimit;

  public BPSOutputFlow(
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "txnVolume") Integer txnVolume,
      @JsonProperty(value = "txnTimeLimit") Integer txnTimeLimit) {
    this.messageType = messageType;
    this.txnVolume = txnVolume;
    this.txnTimeLimit = txnTimeLimit;
  }
}
