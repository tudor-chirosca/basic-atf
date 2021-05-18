package com.vocalink.crossproduct.infrastructure.jpa.audit;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface AuditDetailsView {

  UUID getId();

  String getActivityName();

  String getServiceId();

  ZonedDateTime getTimestamp();

  String getCorrelationId();

  String getEventType();

  String getRequestOrResponseEnum();

  String getResponseContent();

  String getUsername();

  String getFirstName();

  String getLastName();

  String getParticipantId();
}
