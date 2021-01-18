package com.vocalink.crossproduct.ui.dto.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementScheduleDto {

  List<SettlementCycleScheduleDto> weekdayCycles;
  List<SettlementCycleScheduleDto> weekendCycles;
}
