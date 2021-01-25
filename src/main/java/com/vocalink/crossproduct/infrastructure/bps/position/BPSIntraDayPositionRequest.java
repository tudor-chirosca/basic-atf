package com.vocalink.crossproduct.infrastructure.bps.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BPSIntraDayPositionRequest {

  private final String schemeCode;
  private final String participantId;
}
