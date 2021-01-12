package com.vocalink.crossproduct.domain.files;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileEnquirySearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final List<String> cycleIds;
  private final String messageDirection;
  private final String messageType;
  private final String sendingBic;
  private final String receivingBic;
  private final String status;
  private final String reasonCode;
  private final String id;
}
