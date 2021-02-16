package com.vocalink.crossproduct.domain.report;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportSearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
}
