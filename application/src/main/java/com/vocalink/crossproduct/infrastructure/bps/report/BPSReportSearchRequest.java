package com.vocalink.crossproduct.infrastructure.bps.report;

import java.time.ZonedDateTime;
import java.util.List;

import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSReportSearchRequest {

  private final List<BPSSortingQuery> sortingOrder;
  private final List<String> reportTypes;
  private final List<String> participants;
  private final String reportId;
  private final ZonedDateTime createdFromDate;
  private final ZonedDateTime createdToDate;
}
