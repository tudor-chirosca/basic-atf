package com.vocalink.crossproduct.ui.dto.cycle;

import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CycleDto {

  private final String id;
  private final ZonedDateTime settlementTime;
  private final ZonedDateTime cutOffTime;
  private final CycleStatus status;
}
