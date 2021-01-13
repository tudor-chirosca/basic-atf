package com.vocalink.crossproduct.infrastructure.bps.settlement;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class BPSSingleSettlementRequest {

  private final String cycleId;
  private final String participantId;

}
