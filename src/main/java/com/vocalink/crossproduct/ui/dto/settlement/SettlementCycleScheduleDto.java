package com.vocalink.crossproduct.ui.dto.settlement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SettlementCycleScheduleDto {

  private final String cycleName;
  private final String startTime;
  private final String cutOffTime;
  private final String settlementStartTime;
}
