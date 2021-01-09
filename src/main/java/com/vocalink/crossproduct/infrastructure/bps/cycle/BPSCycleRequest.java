package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BPSCycleRequest {

  private final String schemeCode;
  @Setter
  @JsonInclude(Include.NON_EMPTY)
  private Integer numberOfCycles;

  @JsonCreator
  public BPSCycleRequest(@JsonProperty("schemeCode") String schemeCode) {
    this.schemeCode = schemeCode;
  }

}
