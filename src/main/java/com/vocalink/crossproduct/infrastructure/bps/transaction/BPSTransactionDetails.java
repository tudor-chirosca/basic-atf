package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSSenderDetails;
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
  private final BPSSenderDetails sender;
  private final BPSSenderDetails receiver;

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
      @JsonProperty(value = "sender") BPSSenderDetails sender,
      @JsonProperty(value = "receiver") BPSSenderDetails receiver) {
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
    this.sender = sender;
    this.receiver = receiver;
  }
}
