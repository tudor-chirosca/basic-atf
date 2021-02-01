package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum BPSApprovalRequestType {

  @JsonProperty("BATCHCANCELLATION")
  BATCH_CANCELLATION,
  @JsonProperty("PARTICIPANTSTATUSCHANGE")
  STATUS_CHANGE,
  @JsonProperty("PARTICIPANTCONF")
  CONFIG_CHANGE
}
