package com.vocalink.crossproduct.ui.dto.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutingRecordDto {

  private final String reachableBic;
  private final ZonedDateTime validFrom;
  @JsonInclude(Include.NON_EMPTY)
  private final ZonedDateTime validTo;
  @JsonInclude(Include.NON_EMPTY)
  private final String currency;
}
