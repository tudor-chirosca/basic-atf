package com.vocalink.crossproduct.ui.aspects;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AuditableRequest {

  @JsonIgnore
  EventType getEventType();

  @JsonIgnore
  Map<String, Object> getAuditableContent();
}
