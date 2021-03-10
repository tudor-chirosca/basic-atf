package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.controllers.api.ValidationApi;
import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.facade.api.ValidationFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ValidationController implements ValidationApi {

  private final ValidationFacade validationFacade;

  @GetMapping(value = "/validations/approvals/eod", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ValidationApprovalDto> getApprovalValidation(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context) {
    final ValidationApprovalDto approvalValidationDto = validationFacade
        .getApprovalValidation(context, clientType, ZonedDateTime.now());
    return ResponseEntity.ok(approvalValidationDto);
  }
}
