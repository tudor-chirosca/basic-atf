package com.vocalink.crossproduct.domain.approval;

import lombok.Getter;

@Getter
public enum ApprovalRequestType {
  SUSPEND("suspend"),
  CONFIG_CHANGE("config-change"),
  UNSUSPEND("unsuspend"),
  BATCH_CANCELLATION("batch-cancellation");

  private final String property;

  ApprovalRequestType(String property) {
    this.property = property;
  }
}
