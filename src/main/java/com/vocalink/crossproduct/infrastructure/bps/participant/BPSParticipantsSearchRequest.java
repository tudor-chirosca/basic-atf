package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class BPSParticipantsSearchRequest {

  private final String connectingParty;
  private final String participantType;

  public BPSParticipantsSearchRequest() {
    this.connectingParty = null;
    this.participantType = null;
  }
}
