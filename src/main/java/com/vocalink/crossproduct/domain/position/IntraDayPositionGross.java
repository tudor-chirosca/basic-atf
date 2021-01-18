package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
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
  private BPSAmount debitCapAmount;
  private BPSAmount debitPositionAmount;
}
