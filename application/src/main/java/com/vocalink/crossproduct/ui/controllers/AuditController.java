package com.vocalink.crossproduct.ui.controllers;


import static com.vocalink.crossproduct.ui.aspects.EventType.AUDIT_LOG_ENQUIRY;
import static com.vocalink.crossproduct.ui.aspects.EventType.AUDIT_LOG_EVENT_DETAILS;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.AuditApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuditController implements AuditApi {

  public static final String CLIENT_TYPE_HEADER = "client-type";

  private final AuditFacade auditFacade;

  @GetMapping(value = "/reference/audit/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDetailsDto>> getUsers(
      @RequestHeader(CLIENT_TYPE_HEADER) final ClientType clientType,
      @RequestHeader final String context,
      final String participantId) {

    List<UserDetailsDto> users = auditFacade.getUserDetails(context, clientType, participantId);

    return ResponseEntity.ok(users);
  }

  @GetMapping(value = "/reference/audit/events", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<String>> getEvents(
      @RequestHeader(CLIENT_TYPE_HEADER) final ClientType clientType,
      @RequestHeader final String context) {

    List<String> events = auditFacade.getEventTypes(context, clientType);

    return ResponseEntity.ok(events);
  }

  @Auditable(type = AUDIT_LOG_ENQUIRY, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/audits", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<AuditDto>> getAuditLog(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader final String context,
      final AuditRequestParams parameters,
      final HttpServletRequest httpServletRequest) {

    PageDto<AuditDto> auditDetailsDto = auditFacade.getAuditLogs(context, clientType, parameters);

    return ResponseEntity.ok(auditDetailsDto);
  }

  @Auditable(type = AUDIT_LOG_EVENT_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/audits/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuditDetailsDto> getAuditDetails(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader final String context,
      @PathVariable final String id,
      final HttpServletRequest httpServletRequest) {

    AuditDetailsDto detailsDto = auditFacade.getAuditDetails(context, clientType, id);

    return ResponseEntity.ok(detailsDto);
  }
}
