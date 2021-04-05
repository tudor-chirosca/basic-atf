package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSIODashboard {

  private final String fileRejected;
  private final String batchesRejected;
  private final String transactionsRejected;
  private final List<BPSParticipantIOData> summary;

  @JsonCreator
  public BPSIODashboard(
      @JsonProperty(value = "fileRejected", required = true) String fileRejected,
      @JsonProperty(value = "batchesRejected", required = true) String batchesRejected,
      @JsonProperty(value = "transactionsRejected", required = true) String transactionsRejected,
      @JsonProperty(value = "summary", required = true) List<BPSParticipantIOData> summary) {
    this.fileRejected = fileRejected;
    this.batchesRejected = batchesRejected;
    this.transactionsRejected = transactionsRejected;
    this.summary = summary;
  }
}
