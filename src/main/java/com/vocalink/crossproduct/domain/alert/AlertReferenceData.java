package com.vocalink.crossproduct.domain.alert;

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
public class AlertReferenceData {

  private List<AlertPriority> priorities;
  private List<String> alertTypes;
}
