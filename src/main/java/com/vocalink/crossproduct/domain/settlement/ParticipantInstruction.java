package com.vocalink.crossproduct.domain.settlement;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantInstruction {

  private final String cycleId;
  private final String participantId;
  private final String reference;
  private final String status;
  private final String counterpartyId;
  private final String settlementCounterpartyId;
  private final BigDecimal totalDebit;
  private final BigDecimal totalCredit;
}
