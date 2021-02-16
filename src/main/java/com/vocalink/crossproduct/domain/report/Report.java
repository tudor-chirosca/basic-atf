package com.vocalink.crossproduct.domain.report;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Report {

  private final String reportId;
  private final String reportType;
  private final ZonedDateTime createdAt;
  private final String cycleId;
  private final String participantIdentifier;
  private final String participantName;
}
