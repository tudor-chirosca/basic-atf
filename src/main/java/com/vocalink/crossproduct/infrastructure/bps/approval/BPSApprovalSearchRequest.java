package com.vocalink.crossproduct.infrastructure.bps.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSApprovalSearchRequest {

  private final int offset;
  private final int pageSize;
}
