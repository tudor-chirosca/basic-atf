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
  private final ZonedDateTime settlementDate;
  private final String schemeParticipantIdentifier;

  @JsonCreator
  public BPSFile(
      @JsonProperty(value = "instructionId") String instructionId,
      @JsonProperty(value = "fileName") String fileName,
      @JsonProperty(value = "fileSize") Long fileSize,
      @JsonProperty(value = "createdDate") ZonedDateTime createdDate,
      @JsonProperty(value = "originator") String originator,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "noOfBatches") Integer nrOfBatches,
      @JsonProperty(value = "status") String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "settlementCycle") String settlementCycle,
      @JsonProperty(value = "settlementDate") ZonedDateTime settlementDate,
      @JsonProperty(value = "schemeParticipantIdentifier") String schemeParticipantIdentifier) {
    this.instructionId = instructionId;
    this.fileName = fileName;
    this.fileSize = fileSize == null ? 0 : fileSize;
    this.createdDate = createdDate;
    this.originator = originator;
    this.messageType = messageType;
    this.nrOfBatches = nrOfBatches == null ? 0 : nrOfBatches;
    this.status = status;
    this.reasonCode = reasonCode;
    this.settlementCycle = settlementCycle;
    this.settlementDate = settlementDate;
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
  }
}
