package com.vocalink.crossproduct.domain.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertStatsData {

  private final AlertPriorityType priority;
  private final int count;
}
