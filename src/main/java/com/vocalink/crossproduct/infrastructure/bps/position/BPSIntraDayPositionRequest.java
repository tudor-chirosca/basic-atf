package com.vocalink.crossproduct.infrastructure.bps.position;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSIntraDayPositionRequest {

  private final String schemeCode;
  private final String participantId;
}
