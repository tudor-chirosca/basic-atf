package com.vocalink.crossproduct.domain.audit;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Event {

  private final String product;
  private final String client;
  private final String requestUrl;
  private final String userId;
  private final String participantId;
  private final String content;
  private final String correlationId;
  private final String eventType;
  private final String operationType;
  private final String approvalRequestId;
  private final String ipAddress;
  private final String userRoleList;
  private final String scheme;
  private final String customer;
}
