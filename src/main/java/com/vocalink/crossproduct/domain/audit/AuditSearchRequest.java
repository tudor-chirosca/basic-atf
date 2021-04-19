package com.vocalink.crossproduct.domain.audit;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuditSearchRequest {

  private final Integer offset;
  private final Integer limit;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final String participant;
  private final String user;
  private final List<String> events;
  private List<String> sort;
}
