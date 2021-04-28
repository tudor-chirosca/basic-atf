package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class BPSParticipantsSearchRequest {

  private String connectingParty;
  private String participantType;
  private List<BPSSortingQuery> sortingOrder;

  public BPSParticipantsSearchRequest() {
    this.connectingParty = null;
    this.participantType = null;
    this.sortingOrder = null;
  }
}
