package com.vocalink.crossproduct.ui.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class AuditDto {

  private final UUID id;
  @JsonIgnore
  private final UUID activityId;
  private final Long service;
  private final ZonedDateTime createdAt;
  @Setter
  private String eventType;
  private final String data;
  private final UserDto user;
}
