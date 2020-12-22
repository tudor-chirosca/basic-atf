package com.vocalink.crossproduct.ui.dto.settlement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
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
  private final ParticipantReferenceDto counterparty;
  @JsonInclude(Include.NON_EMPTY)
  private final ParticipantReferenceDto settlementCounterparty;
  private final BigDecimal totalDebit;
  private final BigDecimal totalCredit;
}
