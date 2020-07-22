package com.vocalink.portal.domain;

import com.vocalink.portal.ui.dto.ParticipantPositionDto;
import com.vocalink.portal.ui.dto.PositionItemDto;
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
