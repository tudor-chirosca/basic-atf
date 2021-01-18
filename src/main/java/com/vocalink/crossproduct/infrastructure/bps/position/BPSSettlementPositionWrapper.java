package com.vocalink.crossproduct.infrastructure.bps.position;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSSettlementPositionWrapper {

  private final String schemeId;
  private final List<BPSSettlementPosition> mlSettlementPositions;

  @JsonCreator
  public BPSSettlementPositionWrapper(
      @JsonProperty(value = "schemeId", required = true) String schemeId,
      @JsonProperty(value = "mlSettlementPositions") List<BPSSettlementPosition> mlSettlementPositions) {

    this.schemeId = schemeId;
    this.mlSettlementPositions = mlSettlementPositions;
  }
}
