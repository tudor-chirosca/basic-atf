package com.vocalink.crossproduct.ui.dto.alert;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertReferenceDataDto {

  private List<AlertPriorityDto> priorities;
  private List<String> alertTypes;
}
