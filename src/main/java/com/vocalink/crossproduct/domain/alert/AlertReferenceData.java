package com.vocalink.crossproduct.domain.alert;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertReferenceData {

  private final List<AlertPriorityData> priorities;
  private final List<String> alertTypes;
}
