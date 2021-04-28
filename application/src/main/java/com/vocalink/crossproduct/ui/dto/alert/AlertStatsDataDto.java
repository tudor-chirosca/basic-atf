package com.vocalink.crossproduct.ui.dto.alert;

import com.vocalink.crossproduct.domain.alert.AlertPriorityType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertStatsDataDto {

  private final AlertPriorityType priority;
  private final int count;
}
