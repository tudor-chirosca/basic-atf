package com.vocalink.crossproduct.infrastructure.bps.cycle;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSCycleWrapper {

  private final String currency;
  private final String schemeCode;
  private final List<BPSCycle> cycles;

  @JsonCreator
  public BPSCycleWrapper(@JsonProperty("currency") String currency,
      @JsonProperty("schemeCode") String schemeCode,
      @JsonProperty("cycles") List<BPSCycle> cycles) {
    this.currency = currency;
    this.schemeCode = schemeCode;
    this.cycles = cycles == null ? emptyList() : cycles;
  }

}
