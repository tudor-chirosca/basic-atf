package com.vocalink.crossproduct.infrastructure.bps.participant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSParticipantConfigurationRequest {

  private final String schemeParticipantIdentifier;
}
