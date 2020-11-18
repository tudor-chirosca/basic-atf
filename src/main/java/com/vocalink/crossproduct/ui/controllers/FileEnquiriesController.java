package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.infrastructure.exception.InvalidRequestParameterException;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.FileEnquiriesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FileEnquiriesController implements FileEnquiriesApi {

  private final FileEnquiriesFacade fileEnquiriesFacade;

  @GetMapping(value = "/enquiry/files", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto> getFileEnquiries(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final FileEnquirySearchRequest request) {

    if (request.getMessageDirection() == null) {
      throw new InvalidRequestParameterException("msg_direction is missing in request params");
    }

    if (request.getCycleIds() != null && !request.getCycleIds().isEmpty() &&
        (request.getDateFrom() != null || request.getDateTo() != null)) {
      throw new InvalidRequestParameterException("cycleIds and date are included in request params");
    }

    if (request.getSendingBic() != null && request.getReceivingBic() != null &&
        request.getSendingBic().equals(request.getReceivingBic())) {

      throw new InvalidRequestParameterException("Sending and Receiving BIC are the same");
    }

    PageDto fileEnquiryDto = fileEnquiriesFacade
        .getFileEnquiries(context, clientType, request);

    return ResponseEntity.ok().body(fileEnquiryDto);
  }
}
