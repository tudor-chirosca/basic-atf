package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.domain.Amount;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IntraDayPositionGross {

  private final String schemeId;
  private final String debitParticipantId;
  private final LocalDate settlementDate;
  private final Amount debitCapAmount;
  private final Amount debitPositionAmount;
}
