package com.vocalink.crossproduct.ui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantDashboardSettlementDetailsDto {

  private final ParticipantDto participant;
  private final CycleDto currentCycle;
  private final CycleDto previousCycle;
  private final PositionDetailsDto currentPosition;
  private final PositionDetailsDto previousPosition;
  private final PositionDetailsTotalsDto previousPositionTotals;
  private final PositionDetailsTotalsDto currentPositionTotals;

  @JsonInclude(Include.NON_EMPTY)
  private final ParticipantDto settlementBank;

  @JsonInclude(Include.NON_EMPTY)
  private final IntraDayPositionGrossDto intraDayPositionGross;
}


