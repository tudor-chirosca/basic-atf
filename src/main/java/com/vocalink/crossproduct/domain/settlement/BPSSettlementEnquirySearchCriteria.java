package com.vocalink.crossproduct.domain.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BPSSettlementEnquirySearchCriteria {

  private int offset;
  private int limit;
  private List<String> sort;
  private String dateFrom;
  private String dateTo;
  private final List<String> cycleIds;
  private final List<String> participants;
}
