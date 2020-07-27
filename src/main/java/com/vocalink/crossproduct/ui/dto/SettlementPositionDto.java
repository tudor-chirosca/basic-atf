package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.Participant;
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
public class SettlementPositionDto {
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
