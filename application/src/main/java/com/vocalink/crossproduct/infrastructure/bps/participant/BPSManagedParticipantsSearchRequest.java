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
public class BPSManagedParticipantsSearchRequest {

  private int offset;
  private int limit;
  private List<BPSSortingQuery> sortingOrder;
  private String q;
}
