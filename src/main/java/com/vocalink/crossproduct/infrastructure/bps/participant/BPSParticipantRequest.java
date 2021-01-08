package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BPSParticipantRequest {

  @JsonInclude(Include.NON_EMPTY)
  private String schemeParticipantIdentifier;
  @JsonInclude(Include.NON_EMPTY)
  private String connectingParty;
  @JsonInclude(Include.NON_EMPTY)
  private String participantType;
}
