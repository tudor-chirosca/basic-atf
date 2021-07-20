package com.vocalink.crossproduct.infrastructure.bps.approval;

import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.APPROVAL_CREATE_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.BATCH_APPROVAL_CREAT_PATH;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum BPSApprovalRequestType {

  @JsonProperty("BATCHCANCELLATION")
  BATCH_CANCELLATION(BATCH_APPROVAL_CREAT_PATH),
  @JsonProperty("TRANSACTIONCANCELLATION")
  TRANSACTION_CANCELLATION(APPROVAL_CREATE_PATH),
  PARTICIPANT_SUSPEND(APPROVAL_CREATE_PATH),
  PARTICIPANT_UNSUSPEND(APPROVAL_CREATE_PATH),
  @JsonProperty("PARTICIPANTCONF")
  CONFIG_CHANGE(APPROVAL_CREATE_PATH),
  FILE_UPLOAD(APPROVAL_CREATE_PATH);

  private final String approvalRequestPath;

  BPSApprovalRequestType(String approvalRequestPath) {
    this.approvalRequestPath = approvalRequestPath;
  }
}
