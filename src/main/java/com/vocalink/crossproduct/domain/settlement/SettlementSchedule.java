package com.vocalink.crossproduct.domain.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementSchedule {

  private final String weekDay;
  private final List<SettlementCycleSchedule> cycles;

}
