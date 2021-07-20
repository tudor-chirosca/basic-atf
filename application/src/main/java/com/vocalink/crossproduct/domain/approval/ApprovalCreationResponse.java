package com.vocalink.crossproduct.domain.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalCreationResponse {

  private final String requestId;
  private final String status;
}
