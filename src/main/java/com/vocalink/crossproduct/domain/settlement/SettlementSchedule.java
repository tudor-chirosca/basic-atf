package com.vocalink.crossproduct.domain.settlement;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SettlementSchedule {

  private final ZonedDateTime updatedAt;
  private final List<ScheduleDayDetails> scheduleDayDetails;
}
