package com.vocalink.crossproduct.domain.approval;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApprovalConfirmation {

  private final String approvalId;
  private final ApprovalConfirmationType action;
  private final String message;
}
