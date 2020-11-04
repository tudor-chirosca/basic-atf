package com.vocalink.crossproduct.ui.dto.alert;

import java.time.LocalDateTime;
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
public class AlertDto {

  private Integer alertId;
  private String priority;
  private LocalDateTime dateRaised;
  private String type;
  private String entity;
}
