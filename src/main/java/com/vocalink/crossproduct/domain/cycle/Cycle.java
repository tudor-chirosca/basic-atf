package com.vocalink.crossproduct.domain.cycle;

import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import java.time.ZonedDateTime;
import java.util.List;
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
}
