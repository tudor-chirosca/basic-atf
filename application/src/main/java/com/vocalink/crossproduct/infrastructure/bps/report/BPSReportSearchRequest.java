package com.vocalink.crossproduct.infrastructure.bps.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSReportSearchRequest {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final List<String> reportTypes;
  private final List<String> participants;
  private final String id;
  @JsonProperty(value = "date_from")
  private final ZonedDateTime dateFrom;
  @JsonProperty(value = "date_to")
  private final ZonedDateTime dateTo;
}
