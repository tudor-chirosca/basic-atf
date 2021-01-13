package com.vocalink.crossproduct.infrastructure.bps.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSSettlementEnquiryRequest {

  private int offset;
  private int limit;
  private List<String> sort;
  private String dateFrom;
  private String dateTo;
  private final List<String> cycleIds;
  private final List<String> participants;
}
