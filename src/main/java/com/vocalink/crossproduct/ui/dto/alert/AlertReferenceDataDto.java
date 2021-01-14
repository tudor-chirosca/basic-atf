package com.vocalink.crossproduct.ui.dto.alert;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlertReferenceDataDto {

  private final List<AlertPriorityDataDto> priorities;
  private final List<String> alertTypes;
}
