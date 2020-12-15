package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantSettlement {

  private final String cycleId;
  private final String status;
  private final String participantId;
  private final Page<ParticipantInstruction> instructions;

}
