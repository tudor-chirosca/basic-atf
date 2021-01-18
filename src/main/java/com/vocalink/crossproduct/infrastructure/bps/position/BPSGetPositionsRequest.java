package com.vocalink.crossproduct.infrastructure.bps.position;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BPSGetPositionsRequest {

  private final String schemeCode;
  private final String currency;
  private final List<String> participantIds;
  private final Integer numberOfCycles;
}
