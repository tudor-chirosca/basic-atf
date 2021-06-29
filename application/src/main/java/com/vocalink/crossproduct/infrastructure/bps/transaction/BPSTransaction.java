package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSTransaction {

  private final String instructionId;
  private final ZonedDateTime createdDateTime;
  private final String debtor;
  private final String creditor;
  private final String messageType;
  private final BPSAmount amount;
  private final String status;

  public BPSTransaction(
      @JsonProperty(value = "instructionId") String instructionId,
      @JsonProperty(value = "createdDateTime") ZonedDateTime createdDateTime,
      @JsonProperty(value = "debtor") String debtor,
      @JsonProperty(value = "creditor") String creditor,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "amount") BPSAmount amount,
      @JsonProperty(value = "status") String status) {
    this.instructionId = instructionId;
    this.createdDateTime = createdDateTime;
    this.debtor = debtor;
    this.creditor = creditor;
    this.messageType = messageType;
    this.amount = amount;
    this.status = status;
  }
}
