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
  private final String receiverEntityName;
  private final String receiverEntityBic;
  private final String receiverIban;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final String senderEntityName;
  private final String senderEntityBic;
  private final String senderIban;
  private final String senderFullName;
  private final String messageDirection;

  @JsonCreator
  public BPSTransaction(
      @JsonProperty(value = "instructionId", required = true) String instructionId,
      @JsonProperty(value = "amount", required = true) BPSAmount amount,
      @JsonProperty(value = "fileName", required = true) String fileName,
      @JsonProperty(value = "batchId", required = true) String batchId,
      @JsonProperty(value = "valueDate", required = true) LocalDate valueDate,
      @JsonProperty(value = "receiverEntityName") String receiverEntityName,
      @JsonProperty(value = "receiverEntityBic") String receiverEntityBic,
      @JsonProperty(value = "receiverIban") String receiverIban,
      @JsonProperty(value = "settlementDate", required = true) LocalDate settlementDate,
      @JsonProperty(value = "settlementCycleId", required = true) String settlementCycleId,
      @JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "senderEntityName", required = true) String senderEntityName,
      @JsonProperty(value = "senderEntityBic", required = true) String senderEntityBic,
      @JsonProperty(value = "senderIban") String senderIban,
      @JsonProperty(value = "senderFullName") String senderFullName,
      @JsonProperty(value = "messageDirection", required = true) String messageDirection) {
    this.instructionId = instructionId;
    this.amount = amount;
    this.fileName = fileName;
    this.batchId = batchId;
    this.valueDate = valueDate;
    this.receiverEntityName = receiverEntityName;
    this.receiverEntityBic = receiverEntityBic;
    this.receiverIban = receiverIban;
    this.settlementDate = settlementDate;
    this.settlementCycleId = settlementCycleId;
    this.createdAt = createdAt;
    this.status = status;
    this.reasonCode = reasonCode;
    this.messageType = messageType;
    this.senderEntityName = senderEntityName;
    this.senderEntityBic = senderEntityBic;
    this.senderIban = senderIban;
    this.senderFullName = senderFullName;
    this.messageDirection = messageDirection;
  }
}
