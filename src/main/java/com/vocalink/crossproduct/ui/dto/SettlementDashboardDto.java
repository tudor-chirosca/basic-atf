package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SettlementDashboardDto {
  private CycleDto currentCycle;
  private CycleDto previousCycle;
  private List<TotalPositionDto> positions;
}
