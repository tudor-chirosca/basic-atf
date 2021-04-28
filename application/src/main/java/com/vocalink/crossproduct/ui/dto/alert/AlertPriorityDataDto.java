package com.vocalink.crossproduct.ui.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertPriorityDataDto {

  private final String name;
  private final Integer threshold;
  private final Boolean highlight;
}
