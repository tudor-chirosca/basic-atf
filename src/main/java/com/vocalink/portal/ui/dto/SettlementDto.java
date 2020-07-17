package com.vocalink.portal.ui.dto;

import com.vocalink.portal.domain.PositionRow;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SettlementDto {
  private CycleDto currentCycle;
  private CycleDto previousCycle;
  private List<PositionRow> positions;
}
