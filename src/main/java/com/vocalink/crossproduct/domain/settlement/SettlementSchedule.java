package com.vocalink.crossproduct.domain.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementSchedule {

  private final List<SettlementCycleSchedule> weekdayCycles;
  private final List<SettlementCycleSchedule> weekendCycles;
}
