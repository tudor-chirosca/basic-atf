package com.vocalink.crossproduct.ui.dto.settlement;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementScheduleDto {

  private final List<SettlementCycleScheduleDto> weekdayCycles;
  private final List<SettlementCycleScheduleDto> weekendCycles;
  private final ZonedDateTime updatedAt;
}
