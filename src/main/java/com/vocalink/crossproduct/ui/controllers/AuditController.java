package com.vocalink.crossproduct.ui.controllers;


import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.controllers.api.AuditApi;
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.facade.api.AuditFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;
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

  @GetMapping(value = "/audits", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<AuditDto>> getAuditLog(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader final String context,
      final AuditRequestParams parameters) {

    Page<AuditDto> auditDetailsDto = auditFacade.getAuditLogs(context, clientType, parameters);

    return ResponseEntity.ok(auditDetailsDto);
  }

  @GetMapping(value = "/audits/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AuditDetailsDto> getAuditDetails(
      @RequestHeader(CLIENT_TYPE_HEADER) ClientType clientType,
      @RequestHeader final String context,
      @PathVariable final String serviceId) {

    AuditDetailsDto detailsDto = auditFacade.getAuditDetails(context, clientType, serviceId);

    return ResponseEntity.ok(detailsDto);
  }
}
