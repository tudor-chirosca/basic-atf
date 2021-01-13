package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.shared.settlement.SettlementStatus;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlement {

  private final String cycleId;
  private final ZonedDateTime settlementTime;
  private final SettlementStatus status;
  private final String participantId;
  private final Page<ParticipantInstruction> instructions;
}
