package com.vocalink.crossproduct.ui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SettlementDashboardDto {

  @JsonInclude(Include.NON_EMPTY)
  private final ParticipantDto fundingParticipant;
  private final CycleDto currentCycle;
  private final CycleDto previousCycle;
  private final List<TotalPositionDto> positions;
  private final ParticipantDashboardSettlementDetailsDto settlementDetails;
  @JsonInclude(Include.NON_EMPTY)
  private final PositionDetailsTotalsDto previousPositionTotals;
  @JsonInclude(Include.NON_EMPTY)
  private final PositionDetailsTotalsDto currentPositionTotals;
  @JsonInclude(Include.NON_EMPTY)
  private final IntraDayPositionTotalDto intraDayPositionTotals;
}
