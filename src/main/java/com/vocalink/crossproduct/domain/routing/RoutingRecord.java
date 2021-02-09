package com.vocalink.crossproduct.domain.routing;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutingRecord {

  private final String reachableBic;
  private final ZonedDateTime validFrom;
  private final ZonedDateTime validTo;
  private final String currency;
}
