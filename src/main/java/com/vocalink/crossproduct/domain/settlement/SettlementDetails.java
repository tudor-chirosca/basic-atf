package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.Amount;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementDetails {

  private final String schemeParticipantIdentifier;
  private final String settlementBank;
  private final String cycleId;
  private final ZonedDateTime settlementCycleDate;
  private final SettlementStatus status;
  private final Integer settlementInstructionReference;
  private final InstructionStatus statusDetail;
  private final String counterParty;
  private final String counterPartySettlement;
  private final Amount totalAmountDebited;
  private final Amount totalAmountCredited;
}
