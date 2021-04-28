package com.vocalink.crossproduct.infrastructure.bps.participant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BPSParticipantSearchRequest {

  private final String schemeParticipantIdentifier;
}
