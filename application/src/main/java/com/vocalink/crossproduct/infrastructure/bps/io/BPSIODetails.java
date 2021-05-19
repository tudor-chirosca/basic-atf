package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSIODetails {

  private final BPSIOFileSummary files;
  private final List<BPSIOBatchSummary> batches;
  private final List<BPSIOTransactionSummary> transactions;

  @JsonCreator
  public BPSIODetails(
      @JsonProperty(value = "files") BPSIOFileSummary files,
      @JsonProperty(value = "batches") List<BPSIOBatchSummary> batches,
      @JsonProperty(value = "transactions") List<BPSIOTransactionSummary> transactions) {
    this.files = files;
    this.batches = batches;
    this.transactions = transactions;
  }
}
