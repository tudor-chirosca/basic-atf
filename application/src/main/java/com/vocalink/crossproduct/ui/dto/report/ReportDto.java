package com.vocalink.crossproduct.ui.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportDto {

  private final String reportId;
  private final String reportType;
  private final ZonedDateTime createdAt;
  @JsonInclude(Include.NON_EMPTY)
  private final String cycleId;
  private final String participantIdentifier;
  private final String participantName;
}
