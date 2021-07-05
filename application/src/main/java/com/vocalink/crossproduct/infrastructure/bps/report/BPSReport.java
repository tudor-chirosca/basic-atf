package com.vocalink.crossproduct.infrastructure.bps.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSReport {

  private final String reportId;
  private final String reportType;
  private final String reportName;
  private final ZonedDateTime reportDate;
  private final String participantName;
  private final String partyCode;
  @JsonInclude(Include.NON_EMPTY)
  private final String cycleId;

  public BPSReport(
      @JsonProperty(value = "reportId", required = true) String reportId,
      @JsonProperty(value = "reportType", required = true) String reportType,
      @JsonProperty(value = "reportName") String reportName,
      @JsonProperty(value = "reportDate", required = true) ZonedDateTime reportDate,
      @JsonProperty(value = "participantName", required = true) String participantName,
      @JsonProperty(value = "partyCode", required = true) String partyCode,
      @JsonProperty(value = "cycleId") String cycleId) {
    this.reportId = reportId;
    this.reportType = reportType;
    this.reportName = reportName;
    this.reportDate = reportDate;
    this.participantName = participantName;
    this.partyCode = partyCode;
    this.cycleId = cycleId;
  }
}
