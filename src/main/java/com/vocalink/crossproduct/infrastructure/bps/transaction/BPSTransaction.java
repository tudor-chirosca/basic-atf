package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSTransaction {

  private final String instructionId;
  private final BPSAmount amount;
  private final String fileName;
  private final String batchId;
  private final LocalDate valueDate;
  private final String receiverParticipantIdentifier;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final String senderParticipantIdentifier;
  private final String messageDirection;

  @JsonCreator
  public BPSTransaction(
      @JsonProperty(value = "instructionId", required = true) String instructionId,
      @JsonProperty(value = "amount", required = true) BPSAmount amount,
      @JsonProperty(value = "fileName", required = true) String fileName,
      @JsonProperty(value = "batchId", required = true) String batchId,
      @JsonProperty(value = "valueDate", required = true) LocalDate valueDate,
      @JsonProperty(value = "receiverParticipantIdentifier") String receiverParticipantIdentifier,
      @JsonProperty(value = "settlementDate", required = true) LocalDate settlementDate,
      @JsonProperty(value = "settlementCycleId", required = true) String settlementCycleId,
      @JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "senderParticipantIdentifier", required = true) String senderParticipantIdentifier,
      @JsonProperty(value = "messageDirection", required = true) String messageDirection) {
    this.instructionId = instructionId;
    this.amount = amount;
    this.fileName = fileName;
    this.batchId = batchId;
    this.valueDate = valueDate;
    this.receiverParticipantIdentifier = receiverParticipantIdentifier;
    this.settlementDate = settlementDate;
    this.settlementCycleId = settlementCycleId;
    this.createdAt = createdAt;
    this.status = status;
    this.reasonCode = reasonCode;
    this.messageType = messageType;
    this.senderParticipantIdentifier = senderParticipantIdentifier;
    this.messageDirection = messageDirection;
  }
}
