package com.vocalink.crossproduct.domain.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementCycleSchedule {

  private final String cycleName;
  private final String startTime;
  private final String cutOffTime;
  private final String settlementStartTime;
}
