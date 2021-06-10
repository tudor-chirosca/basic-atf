package com.vocalink.crossproduct.infrastructure.bps.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSBatchDetailed {

  private final String instructionId;
  private final String batchId;
  private final Integer numberOfTransactions;
  private final String messageType;
  private final ZonedDateTime sentDateAndTime;
  private final String status;
  private final String reasonCode;
  private final String settlementCycle;
  private final ZonedDateTime settlementDate;
  private final String fileName;
  private final String senderBank;
  private final String senderBic;
  private final String senderIban;

  public BPSBatchDetailed(@JsonProperty("instructionId") String instructionId,
      @JsonProperty("batchID") String batchId,
      @JsonProperty("numberOfTransactions") Integer numberOfTransactions,
      @JsonProperty("messageType") String messageType,
      @JsonProperty("sentDateAndTime") ZonedDateTime sentDateAndTime,
      @JsonProperty("status") String status,
      @JsonProperty("reasonCode") String reasonCode,
      @JsonProperty("settlementCycle") String settlementCycle,
      @JsonProperty("settlementDate") ZonedDateTime settlementDate,
      @JsonProperty("fileName") String fileName,
      @JsonProperty("senderBank") String senderBank,
      @JsonProperty("senderBic") String senderBic,
      @JsonProperty("senderIban") String senderIban) {
    this.instructionId = instructionId;
    this.batchId = batchId;
    this.numberOfTransactions = numberOfTransactions;
    this.messageType = messageType;
    this.sentDateAndTime = sentDateAndTime;
    this.status = status;
    this.reasonCode = reasonCode;
    this.settlementCycle = settlementCycle;
    this.settlementDate = settlementDate;
    this.fileName = fileName;
    this.senderBank = senderBank;
    this.senderBic = senderBic;
    this.senderIban = senderIban;
  }
}
