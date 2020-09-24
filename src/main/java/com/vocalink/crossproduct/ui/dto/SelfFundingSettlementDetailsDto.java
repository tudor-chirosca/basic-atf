package com.vocalink.crossproduct.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SelfFundingSettlementDetailsDto {
  private ParticipantDto participant;
  private CycleDto currentCycle;
  private CycleDto previousCycle;
  private PositionDetailsDto customerCreditTransfer;
  private PositionDetailsDto paymentReturn;
  private PositionDetailsTotalsDto previousPositionTotals;
  private PositionDetailsTotalsDto currentPositionTotals;
}


