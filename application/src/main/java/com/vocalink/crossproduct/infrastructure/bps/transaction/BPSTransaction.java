package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSTransaction {

  private final String instructionId;
  private final ZonedDateTime createdDateTime;
  private final String senderBic;
  private final String receiverBic;
  private final String messageType;
  private final BPSAmount amount;
  private final String status;

  public BPSTransaction(
      @JsonProperty(value = "instructionId") String instructionId,
      @JsonProperty(value = "createdDateTime") ZonedDateTime createdDateTime,
      @JsonProperty(value = "senderBic") String senderBic,
      @JsonProperty(value = "receiverBic") String receiverBic,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "amount") BPSAmount amount,
      @JsonProperty(value = "status") String status) {
    this.instructionId = instructionId;
    this.createdDateTime = createdDateTime;
    this.senderBic = senderBic;
    this.receiverBic = receiverBic;
    this.messageType = messageType;
    this.amount = amount;
    this.status = status;
  }
}
