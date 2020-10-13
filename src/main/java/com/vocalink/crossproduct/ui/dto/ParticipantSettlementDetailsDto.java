package com.vocalink.crossproduct.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ParticipantSettlementDetailsDto {

  private ParticipantDto participant;
  private CycleDto currentCycle;
  private CycleDto previousCycle;
  private PositionDetailsDto currentPosition;
  private PositionDetailsDto previousPosition;
  private PositionDetailsTotalsDto previousPositionTotals;
  private PositionDetailsTotalsDto currentPositionTotals;

  private ParticipantDto settlementBank;
  private IntraDayPositionGrossDto intraDayPositionGross;
}


