package com.vocalink.crossproduct.domain.cycle;

import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cycle {

  private String id;
  private LocalDateTime settlementTime;
  private LocalDateTime cutOffTime;
  private CycleStatus status;
  private List<ParticipantPosition> totalPositions;

}
