package com.vocalink.crossproduct.domain.alert;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertStats {

  private final int total;
  private final List<AlertStatsData> items;
}
