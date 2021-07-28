package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class BPSParticipantConfigurationRequest {

  private final String schemeParticipantIdentifier;
  private final String currency;
  private final ParticipantType participantType;
}
