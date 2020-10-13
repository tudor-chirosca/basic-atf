package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.CycleStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class CycleDto {
  private String id;
  private LocalDateTime settlementTime;
  private LocalDateTime cutOffTime;
  private CycleStatus status;
}
