package com.vocalink.crossproduct.domain.report;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportSearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final List<String> reportTypes;
  private final List<String> participants;
  private final String id;
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
}
