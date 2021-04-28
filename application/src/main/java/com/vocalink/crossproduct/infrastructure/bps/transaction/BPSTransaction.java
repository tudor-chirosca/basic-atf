package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
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

  public BPSTransaction(
      @JsonProperty(value = "instructionId") String instructionId,
      @JsonProperty(value = "createdDateTime") ZonedDateTime createdDateTime,
      @JsonProperty(value = "originator") String originator,
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "amount") BPSAmount amount,
      @JsonProperty(value = "status") String status) {
    this.instructionId = instructionId;
    this.createdDateTime = createdDateTime;
    this.originator = originator;
    this.messageType = messageType;
    this.amount = amount;
    this.status = status;
  }
}
