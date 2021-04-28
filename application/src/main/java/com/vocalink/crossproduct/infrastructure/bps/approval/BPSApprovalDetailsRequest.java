package com.vocalink.crossproduct.infrastructure.bps.approval;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BPSApprovalDetailsRequest {

  private final String approvalId;
}
