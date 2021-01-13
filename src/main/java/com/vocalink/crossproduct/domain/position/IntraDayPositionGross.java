package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.infrastructure.bps.position.Amount;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntraDayPositionGross {

  private String schemeId;
  private String debitParticipantId;
  private LocalDate settlementDate;
  private Amount debitCapAmount;
  private Amount debitPositionAmount;
}
