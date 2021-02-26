package com.vocalink.crossproduct.infrastructure.bps.approval;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BPSApprovalConfirmationRequest {

  private final String approvalId;
  private final Boolean isApproved;
  private final String notes;
}
