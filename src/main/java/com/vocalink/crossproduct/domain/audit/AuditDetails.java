package com.vocalink.crossproduct.domain.audit;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuditDetails {

  private final UUID id;
  private final UUID activityId;
  private final ZonedDateTime timestamp;
  private final String customer;
  private final String ipsSuiteApplicationName;
  private final String channel;
  private final String ipAddress;
  private final String username;
  private final String employerOrRepresentation;
  private final String userRoleList;
  private final String userActivityString;
  private final String correlationId;
  private final Long serviceId;
  private final String approvalRequestId;
  private final String requestOrResponseEnum;
  private final String requestUrl;
  private final String contents;
  private final String firstName;
  private final String lastName;
  private final String participantId;
  private final String responseContent;
}
