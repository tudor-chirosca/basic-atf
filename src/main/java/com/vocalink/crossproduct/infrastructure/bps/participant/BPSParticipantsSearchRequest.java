package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class BPSParticipantsSearchRequest {

  private String connectingParty;
  private String participantType;

  public BPSParticipantsSearchRequest() {
    this.connectingParty = null;
    this.participantType = null;
  }
}
