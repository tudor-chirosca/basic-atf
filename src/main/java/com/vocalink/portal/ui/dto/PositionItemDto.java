package com.vocalink.portal.ui.dto;

import com.vocalink.portal.domain.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PositionItemDto {
  private Participant participant;
  private PositionDto previousPosition;
  private PositionDto currentPosition;
}
