package com.vocalink.crossproduct.domain.cycle;

import com.vocalink.crossproduct.domain.position.ParticipantPosition;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Cycle {

  private final String id;
  private final ZonedDateTime settlementTime;
  private final ZonedDateTime cutOffTime;
  private final CycleStatus status;
  private final Boolean isNextDayCycle;
  private final ZonedDateTime settlementConfirmationTime;
  private final List<ParticipantPosition> totalPositions;

  public boolean isNextSettlementCycle(Clock clock) {
    ZonedDateTime eod = ZonedDateTime.now(clock).with(LocalTime.MAX);
    return Objects.nonNull(settlementTime) && eod.isBefore(settlementTime);
  }
  
  public boolean isEmpty() {
    return id == null;
  }
}
