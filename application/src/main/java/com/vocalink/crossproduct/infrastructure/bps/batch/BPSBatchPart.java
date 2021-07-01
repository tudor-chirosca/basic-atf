package com.vocalink.crossproduct.infrastructure.bps.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSBatchPart {

  private final String instructionId;
  private final String messageIdentifier;
  private final ZonedDateTime createdDateTime;
  private final String instructingAgent;
  private final String instructedAgent;
  private final String messageType;
  private final Integer nrOfTransactions;
  private final String status;

  public BPSBatchPart(
      @JsonProperty("instructionId") String instructionId,
      @JsonProperty("messageIdentifier") String messageIdentifier,
      @JsonProperty("createdDateTime") ZonedDateTime createdDateTime,
      @JsonProperty("instructingAgent") String instructingAgent,
      @JsonProperty("instructedAgent") String instructedAgent,
      @JsonProperty("messageType") String messageType,
      @JsonProperty("noOfTransactions") Integer nrOfTransactions,
      @JsonProperty("status") String status) {
    this.instructionId = instructionId;
    this.messageIdentifier = messageIdentifier;
    this.nrOfTransactions = nrOfTransactions;
    this.messageType = messageType;
    this.createdDateTime = createdDateTime;
    this.status = status;
    this.instructingAgent = instructingAgent;
    this.instructedAgent = instructedAgent;
  }
}
