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
  private final ZonedDateTime createdAt;
  @JsonInclude(Include.NON_EMPTY)
  private final String cycleId;
  private final String participantIdentifier;
  private final String participantName;

  public BPSReport(
      @JsonProperty(value = "reportId", required = true) String reportId,
      @JsonProperty(value = "reportType", required = true) String reportType,
      @JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "participantIdentifier", required = true) String participantIdentifier,
      @JsonProperty(value = "participantName", required = true) String participantName) {
    this.reportId = reportId;
    this.reportType = reportType;
    this.createdAt = createdAt;
    this.cycleId = cycleId;
    this.participantIdentifier = participantIdentifier;
    this.participantName = participantName;
  }
}
