package com.vocalink.crossproduct.domain.settlement;

import java.time.ZonedDateTime;
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
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final String cycleId;
  private final List<String> participants;
}
