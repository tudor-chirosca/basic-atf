package com.vocalink.crossproduct.ui.dto.cycle;

import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CycleDto {

  private final String id;
  private final LocalDateTime settlementTime;
  private final LocalDateTime cutOffTime;
  private final CycleStatus status;
}
