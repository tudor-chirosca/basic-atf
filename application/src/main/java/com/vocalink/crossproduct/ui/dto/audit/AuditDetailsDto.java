package com.vocalink.crossproduct.ui.dto.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AuditDetailsDto {

  private final String serviceId;
  private final String eventType;
  private final String product;
  private final ParticipantDetailsDto entity;
  private final UserDetailsDto user;
  private final String customer;
  private final ZonedDateTime requestDate;
  private final ZonedDateTime responseDate;
  private final Object request;
  private final Object response;
  @JsonInclude(Include.NON_EMPTY)
  private final String approvalRequestId;
}
