package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSIODetails {

  private final String schemeParticipantIdentifier;
  private final BPSIODataDetails files;
  private final List<BPSIOBatchesMessageTypes> batches;
  private final List<BPSIOTransactionsMessageTypes> transactions;

  @JsonCreator
  public BPSIODetails(
      @JsonProperty(value = "schemeParticipantIdentifier") String schemeParticipantIdentifier,
      @JsonProperty(value = "files") BPSIODataDetails files,
      @JsonProperty(value = "batches") List<BPSIOBatchesMessageTypes> batches,
      @JsonProperty(value = "transactions") List<BPSIOTransactionsMessageTypes> transactions) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.files = files;
    this.batches = batches;
    this.transactions = transactions;
  }
}
