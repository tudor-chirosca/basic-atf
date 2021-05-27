package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSTransactionDetails {

  private final String txnsInstructionId;
  private final String messageType;
  private final ZonedDateTime sentDateTime;
  private final String transactionStatus;
  private final String reasonCode;
  private final String settlementCycle;
  private final ZonedDateTime settlementDate;
  private final String fileName;
  private final String batchId;
  private final BPSAmount transactionAmount;
  private final String senderBank;
  private final String senderBic;
  private final String senderIBAN;
  private final String senderFullName;
  private final String receiverBank;
  private final String receiverBic;
  private final String receiverIBAN;
  private final String receiverFullName;

  public BPSTransactionDetails(
      @JsonProperty(value = "txnsInstructionId") String txnsInstructionId,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "sentDateTime") ZonedDateTime sentDateTime,
      @JsonProperty(value = "transactionStatus") String transactionStatus,
      @JsonProperty(value = "reasonCode") String reasonCode,
      @JsonProperty(value = "settlementCycle") String settlementCycle,
      @JsonProperty(value = "settlementDate") ZonedDateTime settlementDate,
      @JsonProperty(value = "fileName") String fileName,
      @JsonProperty(value = "batchId") String batchId,
      @JsonProperty(value = "transactionAmount") BPSAmount transactionAmount,
      @JsonProperty(value = "senderBank") String senderBank,
      @JsonProperty(value = "senderBic") String senderBic,
      @JsonProperty(value = "senderIBAN") String senderIBAN,
      @JsonProperty(value = "senderFullName") String senderFullName,
      @JsonProperty(value = "receiverBank") String receiverBank,
      @JsonProperty(value = "receiverBic") String receiverBic,
      @JsonProperty(value = "receiverIBAN") String receiverIBAN,
      @JsonProperty(value = "receiverFullName") String receiverFullName) {
    this.txnsInstructionId = txnsInstructionId;
    this.messageType = messageType;
    this.sentDateTime = sentDateTime;
    this.transactionStatus = transactionStatus;
    this.reasonCode = reasonCode;
    this.settlementCycle = settlementCycle;
    this.settlementDate = settlementDate;
    this.fileName = fileName;
    this.batchId = batchId;
    this.transactionAmount = transactionAmount;
    this.senderBank = senderBank;
    this.senderBic = senderBic;
    this.senderIBAN = senderIBAN;
    this.senderFullName = senderFullName;
    this.receiverBank = receiverBank;
    this.receiverBic = receiverBic;
    this.receiverIBAN = receiverIBAN;
    this.receiverFullName = receiverFullName;
  }
}
