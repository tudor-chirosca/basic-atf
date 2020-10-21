package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
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


