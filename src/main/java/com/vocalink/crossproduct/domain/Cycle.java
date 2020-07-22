package com.vocalink.crossproduct.domain;

import com.vocalink.crossproduct.ui.dto.CycleDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Cycle {
  private String id;
  private String settlementTime;
  private String cutOffTime;
  private CycleStatus status;
  private List<ParticipantPosition> positions;

  public CycleDto toDto(){
    return CycleDto.builder()
        .id(id)
        .settlementTime(settlementTime)
        .cutOffTime(cutOffTime)
        .status(status)
        .build();
  }
}
