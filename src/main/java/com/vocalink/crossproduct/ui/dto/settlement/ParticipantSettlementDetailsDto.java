package com.vocalink.crossproduct.ui.dto.settlement;

import com.vocalink.crossproduct.shared.settlement.SettlementStatus;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlementDetailsDto {

  private final String cycleId;
  private final ZonedDateTime settlementTime;
  private final SettlementStatus status;
  private final ParticipantReferenceDto participant;
  private final PageDto<ParticipantInstructionDto> instructions;
}
