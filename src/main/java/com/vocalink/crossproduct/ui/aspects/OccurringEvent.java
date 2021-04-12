package com.vocalink.crossproduct.ui.aspects;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OccurringEvent {

  private final String product;
  private final String client;
  private final String requestUrl;
  private final String userId;
  private final String participantId;
  private final String content;
  private final String correlationId;
  private final EventType eventType;
  private final OperationType operationType;

  public String getEventType() {
    return eventType.getActivity();
  }
}
