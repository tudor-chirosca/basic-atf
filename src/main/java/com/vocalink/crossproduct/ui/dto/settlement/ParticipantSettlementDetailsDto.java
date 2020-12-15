package com.vocalink.crossproduct.ui.dto.settlement;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlementDetailsDto {

  private final String cycleId;
  private final LocalDateTime settlementTime;
  private final String status;
  private final ParticipantDto participant;
  private final PageDto<ParticipantInstructionDto> instructions;
}
