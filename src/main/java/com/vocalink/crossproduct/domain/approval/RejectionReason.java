package com.vocalink.crossproduct.domain.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RejectionReason {

  private final ApprovalUser rejectedBy;
  private final String comment;
}
