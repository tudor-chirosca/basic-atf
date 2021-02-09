package com.vocalink.crossproduct.domain.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SettlementEnquirySearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final String dateFrom;
  private final String dateTo;
  private final List<String> cycleIds;
  private final List<String> participants;
}
