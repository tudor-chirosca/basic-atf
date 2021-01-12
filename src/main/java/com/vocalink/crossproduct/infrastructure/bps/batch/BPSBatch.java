package com.vocalink.crossproduct.infrastructure.bps.batch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSSenderDetails;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSBatch {

  private final String batchId;
  private final int nrOfTransactions;
  private final String name;
  private final ZonedDateTime createdAt;
  private final long fileSize;
  private final BPSCycle cycle;
  private final String receivingBic;
  private final String messageType;
  private final String messageDirection;
  private final String status;
  private final String reasonCode;
  private final BPSSenderDetails sender;

  @JsonCreator
  public BPSBatch(
      @JsonProperty(value = "batchId", required = true) String batchId,
      @JsonProperty(value = "name", required = true) String name,
      @JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "fileSize", required = true) long fileSize,
      @JsonProperty(value = "cycle", required = true) BPSCycle cycle,
      @JsonProperty(value = "receivingBic", required = true) String receivingBic,
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "messageDirection", required = true) String messageDirection,
      @JsonProperty(value = "nrOfTransactions", required = true) int nrOfTransactions,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "sender", required = true) BPSSenderDetails sender
  ) {
    this.batchId = batchId;
    this.nrOfTransactions = nrOfTransactions;
    this.name = name;
    this.createdAt = createdAt;
    this.fileSize = fileSize;
    this.cycle = cycle;
    this.receivingBic = receivingBic;
    this.messageType = messageType;
    this.messageDirection = messageDirection;
    this.status = status;
    this.reasonCode = reasonCode;
    this.sender = sender;
  }
}
