package com.vocalink.crossproduct.domain.cycle;

import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Cycle {

  private final String id;
  private final LocalDateTime settlementTime;
  private final LocalDateTime cutOffTime;
  private final CycleStatus status;
  private final List<ParticipantPosition> totalPositions;

}
