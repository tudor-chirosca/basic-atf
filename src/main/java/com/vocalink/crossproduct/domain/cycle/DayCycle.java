package com.vocalink.crossproduct.domain.cycle;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DayCycle {

  private final String cycleCode;
  private final String sessionCode;
  private final String sessionInstanceId;
  private final CycleStatus status;
  private final ZonedDateTime createdDate;
  private final ZonedDateTime updatedDate;
}
