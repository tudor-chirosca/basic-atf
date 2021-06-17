package com.vocalink.crossproduct.infrastructure.bps.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSFile {

  private final String instructionId;
  private final String fileName;
  private final long fileSize;
  private final ZonedDateTime createdDate;
  private final String from;
  private final String to;
  private final String messageType;
  private final String messageDirection;
  private final int noOfBatches;
  private final String status;
  private final String reasonCode;
  private final String settlementCycle;

  @JsonCreator
  public BPSFile(
      @JsonProperty(value = "instructionId") String instructionId,
      @JsonProperty(value = "fileName") String fileName,
      @JsonProperty(value = "fileSize") Long fileSize,
      @JsonProperty(value = "createdDate") ZonedDateTime createdDate,
      @JsonProperty(value = "from") String from,
      @JsonProperty(value = "to") String to,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "messageDirection") String messageDirection,
      @JsonProperty(value = "noOfBatches") Integer noOfBatches,
      @JsonProperty(value = "status") String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "settlementCycle") String settlementCycle) {
    this.instructionId = instructionId;
    this.fileName = fileName;
    this.fileSize = fileSize == null ? 0 : fileSize;
    this.createdDate = createdDate;
    this.from = from;
    this.to = to;
    this.messageType = messageType;
    this.messageDirection = messageDirection;
    this.noOfBatches = noOfBatches == null ? 0 : noOfBatches;
    this.status = status;
    this.reasonCode = reasonCode;
    this.settlementCycle = settlementCycle;
  }
}
