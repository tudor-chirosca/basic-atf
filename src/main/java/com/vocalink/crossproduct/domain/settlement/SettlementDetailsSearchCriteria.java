package com.vocalink.crossproduct.domain.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SettlementDetailsSearchCriteria {

  private final int offset;
  private final int limit;
  private final String cycleId;
  private final String participantId;
  private final List<String> sort;
}
