package com.vocalink.crossproduct.ui.dto.settlement;

import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LatestSettlementCyclesDto {
  private final CycleDto previousCycle;
  private final CycleDto currentCycle;
}
