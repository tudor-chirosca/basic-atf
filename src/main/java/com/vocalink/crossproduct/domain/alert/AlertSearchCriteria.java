package com.vocalink.crossproduct.domain.alert;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertSearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> priorities;
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final List<String> types;
  private final List<String> entities;
  private final String alertId;
  private final List<String> sort;
}
