package com.vocalink.crossproduct.domain.cycle;

import java.time.ZonedDateTime;
import java.util.List;

import com.vocalink.crossproduct.domain.position.ParticipantPosition;

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

  public boolean isPreviousDayCycle(Cycle previousCycle) {
    if (previousCycle == null || previousCycle.getSettlementTime() == null) {
      return true;
    }
    return settlementTime.toLocalDate().isAfter(previousCycle.getSettlementTime().toLocalDate());
  }

  public boolean isInEodSodPeriod(Cycle previousCycle) {
    if (CycleStatus.NOT_STARTED.equals(status)) {
      if (previousCycle != null && CycleStatus.OPEN.equals(previousCycle.getStatus())) {
        throw new IllegalStateException("Previous cycle cannot be open when EOD/SOD period");
      }
      return true;
    }
    return false;
  }
  
  public boolean isEmpty() {
    return id == null;
  }
}
