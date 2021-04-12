package com.vocalink.crossproduct.domain.settlement;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlement {

  private final String cycleId;
  private final ZonedDateTime settlementStartDate;
  private final SettlementStatus status;
  private final String schemeParticipantIdentifier;
}
