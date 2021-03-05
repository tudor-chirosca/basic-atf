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
  private final ZonedDateTime createdDateTime;
  private final String originator;
  private final String messageType;
  private final BPSAmount amount;
  private final String status;

  private final String fileName;
  private final String batchId;
  private final LocalDate valueDate;
  private final String receiverParticipantIdentifier;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final String reasonCode;
  private final String senderParticipantIdentifier;
  private final String messageDirection;

  @JsonCreator
  public BPSTransaction(
      @JsonProperty(value = "instructionId", required = true) String instructionId,
      @JsonProperty(value = "amount", required = true) BPSAmount amount,
      @JsonProperty(value = "fileName") String fileName,
      @JsonProperty(value = "batchId") String batchId,
      @JsonProperty(value = "originator", required = true) String originator,
      @JsonProperty(value = "valueDate") LocalDate valueDate,
      @JsonProperty(value = "receiverParticipantIdentifier") String receiverParticipantIdentifier,
      @JsonProperty(value = "settlementDate") LocalDate settlementDate,
      @JsonProperty(value = "settlementCycleId") String settlementCycleId,
      @JsonProperty(value = "createdDateTime", required = true) ZonedDateTime createdDateTime,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "senderParticipantIdentifier") String senderParticipantIdentifier,
      @JsonProperty(value = "messageDirection") String messageDirection) {
    this.instructionId = instructionId;
    this.amount = amount;
    this.fileName = fileName;
    this.batchId = batchId;
    this.originator = originator;
    this.valueDate = valueDate;
    this.receiverParticipantIdentifier = receiverParticipantIdentifier;
    this.settlementDate = settlementDate;
    this.settlementCycleId = settlementCycleId;
    this.createdDateTime = createdDateTime;
    this.status = status;
    this.reasonCode = reasonCode;
    this.messageType = messageType;
    this.senderParticipantIdentifier = senderParticipantIdentifier;
    this.messageDirection = messageDirection;
  }
}
