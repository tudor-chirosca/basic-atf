package com.vocalink.crossproduct.domain.approval;

import lombok.Getter;

@Getter
public enum ApprovalRequestType {

  PARTICIPANT_SUSPEND,
  PARTICIPANT_UNSUSPEND,
  CONFIG_CHANGE,
  BATCH_CANCELLATION
}
