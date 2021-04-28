package com.vocalink.crossproduct.infrastructure.bps.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSAlertSearchRequest {

  private final int offset;

  private final int limit;

  private final List<String> sort;

  private final List<String> priorities;

  @JsonProperty(value = "date_from")
  private final ZonedDateTime dateFrom;

  @JsonProperty(value = "date_to")
  private final ZonedDateTime dateTo;

  private final List<String> types;

  private final List<String> entities;

  private final String alertId;
}
