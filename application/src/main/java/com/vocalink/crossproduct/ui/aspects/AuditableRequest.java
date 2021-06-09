package com.vocalink.crossproduct.ui.aspects;

import java.util.Map;

public interface AuditableRequest {

  EventType getEventType();

  Map<String, Object> getAuditableContent();
}
