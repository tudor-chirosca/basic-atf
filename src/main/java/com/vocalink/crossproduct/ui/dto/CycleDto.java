package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.CycleStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class CycleDto {
  private String id;
  private LocalDateTime settlementTime;
  private LocalDateTime cutOffTime;
  private CycleStatus status;
}
