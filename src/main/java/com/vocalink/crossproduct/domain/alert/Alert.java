package com.vocalink.crossproduct.domain.alert;

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
public class Alert {

  private Integer alertId;
  private String priority;
  private LocalDateTime dateRaised;
  private String type;
  private String entity;
}
