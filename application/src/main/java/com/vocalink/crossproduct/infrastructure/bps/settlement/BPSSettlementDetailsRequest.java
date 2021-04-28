package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSSettlementDetailsRequest {

  private final String cycleId;
  private final String participantId;
  private final List<BPSSortingQuery> sortingOrder;
}
