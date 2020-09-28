package com.vocalink.crossproduct.domain;

import com.vocalink.crossproduct.ui.dto.CycleDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Cycle {

  private String id;
  private LocalDateTime settlementTime;
  private LocalDateTime cutOffTime;
  private CycleStatus status;
  private List<ParticipantPosition> totalPositions;

  public CycleDto toDto() {
    return CycleDto.builder()
        .id(id)
        .settlementTime(settlementTime)
        .cutOffTime(cutOffTime)
        .status(status)
        .build();
  }
}
