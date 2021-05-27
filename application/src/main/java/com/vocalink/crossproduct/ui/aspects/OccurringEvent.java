package com.vocalink.crossproduct.ui.aspects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
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
  @Setter
  private String approvalRequestId;

  public OccurringEvent(OccurringEvent event, String content, OperationType operationType) {
    this.product = event.getProduct();
    this.client = event.getClient();
    this.userId = event.getUserId();
    this.participantId = event.getParticipantId();
    this.requestUrl = event.getRequestUrl();
    this.correlationId = event.getCorrelationId();
    this.eventType = event.getEventType();
    this.content = content;
    this.operationType = operationType;
    this.approvalRequestId = event.getApprovalRequestId();
  }
}
