package com.vocalink.crossproduct.ui.dto.settlement;

import com.vocalink.crossproduct.shared.settlement.SettlementStatus;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlementCycleDto {

  private final String cycleId;
  private final LocalDateTime settlementTime;
  private final SettlementStatus status;
  private final ParticipantReferenceDto participant;
}
