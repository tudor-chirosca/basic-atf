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
  private final String messageDirection;
  private final ZonedDateTime sentDateAndTime;
  private final String status;
  private final String reasonCode;
  private final String settlementCycle;
  private final ZonedDateTime settlementDate;
  private final String fileName;
  private final String instructingAgentName;
  private final String instructingAgent;
  private final String instructedAgentName;
  private final String instructedAgent;


  public BPSBatchDetailed(
      @JsonProperty("instructionId") String instructionId,
      @JsonProperty("batchID") String batchId,
      @JsonProperty("numberOfTransactions") Integer numberOfTransactions,
      @JsonProperty("messageType") String messageType,
      @JsonProperty("messageDirection") String messageDirection,
      @JsonProperty("sentDateAndTime") ZonedDateTime sentDateAndTime,
      @JsonProperty("status") String status,
      @JsonProperty("reasonCode") String reasonCode,
      @JsonProperty("settlementCycle") String settlementCycle,
      @JsonProperty("settlementDate") ZonedDateTime settlementDate,
      @JsonProperty("fileName") String fileName,
      @JsonProperty("instructingAgentName") String instructingAgentName,
      @JsonProperty("instructingAgent") String instructingAgent,
      @JsonProperty("instructedAgentName") String instructedAgentName,
      @JsonProperty("instructedAgent") String instructedAgent){
    this.instructionId = instructionId;
    this.batchId = batchId;
    this.numberOfTransactions = numberOfTransactions;
    this.messageType = messageType;
    this.messageDirection = messageDirection;
    this.sentDateAndTime = sentDateAndTime;
    this.status = status;
    this.reasonCode = reasonCode;
    this.settlementCycle = settlementCycle;
    this.settlementDate = settlementDate;
    this.fileName = fileName;
    this.instructingAgentName = instructingAgentName;
    this.instructingAgent = instructingAgent;
    this.instructedAgentName = instructedAgentName;
    this.instructedAgent = instructedAgent;

  }
}
