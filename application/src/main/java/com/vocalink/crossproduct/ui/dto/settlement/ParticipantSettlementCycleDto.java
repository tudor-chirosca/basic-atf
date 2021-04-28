package com.vocalink.crossproduct.ui.dto.settlement;

import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlementCycleDto {

  private final String cycleId;
  private final ZonedDateTime settlementTime;
  private final CycleStatus status;
  private final ParticipantReferenceDto participant;
}
