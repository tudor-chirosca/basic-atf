package com.vocalink.crossproduct.ui.dto.alert;

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
public class AlertPriorityDto {

  private String name;
  private Integer threshold;
  private Boolean highlight;
}
