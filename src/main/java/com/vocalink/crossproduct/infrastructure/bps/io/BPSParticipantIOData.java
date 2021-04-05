package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSParticipantIOData {

  private final String schemeParticipantIdentifier;
  private final BPSIOData files;
  private final BPSIOData batches;
  private final BPSIOData transactions;

  @JsonCreator
  public BPSParticipantIOData(
      @JsonProperty(value = "schemeParticipantIdentifier") String schemeParticipantIdentifier,
      @JsonProperty(value = "files") BPSIOData files,
      @JsonProperty(value = "batches") BPSIOData batches,
      @JsonProperty(value = "transactions") BPSIOData transactions) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.files = files;
    this.batches = batches;
    this.transactions = transactions;
  }
}

