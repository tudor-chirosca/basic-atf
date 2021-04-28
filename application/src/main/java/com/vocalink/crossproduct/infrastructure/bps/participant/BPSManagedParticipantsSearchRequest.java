package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class BPSManagedParticipantsSearchRequest {

  private int offset;
  private int limit;
  private List<String> sort;
  private String q;
}
