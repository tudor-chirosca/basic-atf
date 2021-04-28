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
  private final String originator;
  private final String messageType;
  private final int nrOfBatches;
  private final String status;
  private final String reasonCode;
  private final String settlementCycle;
  private final String schemeParticipantIdentifier;

  @JsonCreator
  public BPSFile(
      @JsonProperty(value = "instructionId", required = true) String instructionId,
      @JsonProperty(value = "fileName", required = true) String fileName,
      @JsonProperty(value = "fileSize", required = true) long fileSize,
      @JsonProperty(value = "createdDate", required = true) ZonedDateTime createdDate,
      @JsonProperty(value = "originator", required = true) String originator,
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "nrOfBatches", required = true) int nrOfBatches,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "reasonCode", required = true) String reasonCode,
      @JsonProperty(value = "settlementCycle", required = true) String settlementCycle,
      @JsonProperty(value = "schemeParticipantIdentifier", required = true) String schemeParticipantIdentifier) {
    this.instructionId = instructionId;
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.createdDate = createdDate;
    this.originator = originator;
    this.messageType = messageType;
    this.nrOfBatches = nrOfBatches;
    this.status = status;
    this.reasonCode = reasonCode;
    this.settlementCycle = settlementCycle;
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
  }
}
