package com.vocalink.crossproduct.domain.approval;

import lombok.Getter;

@Getter
public enum ApprovalRequestType {

  STATUS_CHANGE,
  CONFIG_CHANGE,
  BATCH_CANCELLATION
}
