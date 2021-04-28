package com.vocalink.crossproduct.domain.settlement;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleDayDetails {

  private final String weekDay;
  private final List<SettlementCycleSchedule> cycles;
}
