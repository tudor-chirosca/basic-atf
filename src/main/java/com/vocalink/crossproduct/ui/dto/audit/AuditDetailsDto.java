package com.vocalink.crossproduct.ui.dto.audit;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AuditDetailsDto {

  private final Long id;
  private final String eventType;
  private final String operationType;
  private final ZonedDateTime timestamp;
  private final String entityId;
  private final String submitter;
  private final String submitterOrganisation;
  private final Object interactionDetails;
}
