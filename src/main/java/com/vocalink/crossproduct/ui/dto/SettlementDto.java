package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.SettlementPosition;
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
  private List<SettlementPosition> positions;
}
