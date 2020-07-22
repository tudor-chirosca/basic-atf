package com.vocalink.crossproduct.domain;

import com.vocalink.crossproduct.ui.dto.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.PositionItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SettlementPosition {
  private Participant participant;
  private ParticipantPositionDto previousPosition;
  private ParticipantPositionDto currentPosition;

  public PositionItemDto toDto(){
    return PositionItemDto.builder()
        .currentPosition(currentPosition)
        .previousPosition(previousPosition)
        .build();
  }
}
