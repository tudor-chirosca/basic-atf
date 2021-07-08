package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum BPSApprovalRequestType {

  @JsonProperty("BATCHCANCELLATION")
  BATCH_CANCELLATION,
  @JsonProperty("TRANSACTIONCANCELLATION")
  TRANSACTION_CANCELLATION,
  PARTICIPANT_SUSPEND,
  PARTICIPANT_UNSUSPEND,
  @JsonProperty("PARTICIPANTCONF")
  CONFIG_CHANGE,
  FILE_UPLOAD
}
