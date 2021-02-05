package com.vocalink.crossproduct.domain.broadcasts;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BroadcastsSearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final String recipient;
  private final String msg;
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
}
