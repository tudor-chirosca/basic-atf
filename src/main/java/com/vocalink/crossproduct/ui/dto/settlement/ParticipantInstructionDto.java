package com.vocalink.crossproduct.ui.dto.settlement;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantInstructionDto {

  private final String reference;
  private final String status;
  private final ParticipantDto counterparty;
  private final ParticipantDto settlementCounterparty;
  private final BigDecimal totalDebit;
  private final BigDecimal totalCredit;
}
