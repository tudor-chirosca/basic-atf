package com.vocalink.portal.domain;

import com.vocalink.portal.ui.dto.PositionItem;
import org.springframework.stereotype.Component;

@Component
public class PositionItemFactory {

  public static PositionItem newInstance(Position currentPosition, Position previousPosition,
      Participant participant) {
    return PositionItem
        .builder()
        .currentPosition(currentPosition)
        .previousPosition(previousPosition)
        .participant(participant)
        .build();
  }
}
