package com.vocalink.crossproduct.domain.approval;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalSearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
}
