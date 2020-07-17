package com.vocalink.portal.domain;

import com.vocalink.portal.ui.dto.PositionDto;
import com.vocalink.portal.ui.dto.PositionItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PositionRow {
  private Participant participant;
  private PositionDto previousPosition;
  private PositionDto currentPosition;

  public PositionItemDto toDto(){
    return PositionItemDto.builder()
        .currentPosition(currentPosition)
        .previousPosition(previousPosition)
        .build();
  }
}
