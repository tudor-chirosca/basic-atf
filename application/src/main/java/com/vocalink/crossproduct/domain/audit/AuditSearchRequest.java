package com.vocalink.crossproduct.domain.audit;

import java.time.ZonedDateTime;
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
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final String participant;
  private final String user;
  private final List<String> events;
  private final List<String> sort;
}
