package com.vocalink.crossproduct.ui.dto.alert;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertStatsDto {

  private final int total;
  private final List<AlertStatsDataDto> items;
}
