package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
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

  private ParticipantDto fundingParticipant;
  private CycleDto currentCycle;
  private CycleDto previousCycle;
  private List<TotalPositionDto> positions;
  private PositionDetailsTotalsDto previousPositionTotals;
  private PositionDetailsTotalsDto currentPositionTotals;
  private IntraDayPositionTotalDto intraDayPositionTotals;
}
