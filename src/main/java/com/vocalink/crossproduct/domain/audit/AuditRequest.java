package com.vocalink.crossproduct.domain.audit;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuditRequest {

  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final String participant;
  private final String user;
  private final String event;
}
