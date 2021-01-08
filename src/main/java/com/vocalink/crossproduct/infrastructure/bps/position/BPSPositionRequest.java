package com.vocalink.crossproduct.infrastructure.bps.position;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BPSPositionRequest {

  private final String settlementDate;
  private final String schemeCode;
  private final List<String> cycleIds;
  private final String currency;
  private final String participantId;
}
