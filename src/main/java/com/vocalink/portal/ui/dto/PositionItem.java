package com.vocalink.portal.ui.dto;

import com.vocalink.portal.domain.Participant;
import com.vocalink.portal.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PositionItem {

  private Participant participant;
  private Position previousPosition;
  private Position currentPosition;
}
