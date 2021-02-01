package com.vocalink.crossproduct.domain.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalSearchCriteria {

  private final int offset;
  private final int limit;
}
