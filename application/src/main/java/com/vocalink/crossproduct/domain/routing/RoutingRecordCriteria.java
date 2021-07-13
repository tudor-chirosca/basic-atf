package com.vocalink.crossproduct.domain.routing;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutingRecordCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final String participantId;
}
