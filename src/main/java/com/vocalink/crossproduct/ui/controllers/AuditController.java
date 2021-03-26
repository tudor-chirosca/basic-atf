package com.vocalink.crossproduct.ui.controllers;


import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.controllers.api.AuditApi;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuditController implements AuditApi {

  private final AuditFacade auditFacade;

  @GetMapping(value = "/reference/audit/users", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UserDetailsDto>> getUser(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      final String participantId) {

    List<UserDetailsDto> users = auditFacade.getUserDetails(context, clientType, participantId);

    return ResponseEntity.ok(users);
  }

  @GetMapping(value = "/audits", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<AuditDto>> getAuditLog(@RequestHeader("client-type") ClientType clientType,
      @RequestHeader final String context,
      final AuditRequestParams parameters) {

    Page<AuditDto> auditDetailsDto = auditFacade.getAuditLogs(context, clientType, parameters);

    return ResponseEntity.ok(auditDetailsDto);
  }
}
