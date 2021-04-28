package com.vocalink.crossproduct.domain.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertPriorityData {

  private final String name;
  private final Integer threshold;
  private final Boolean highlight;
}
