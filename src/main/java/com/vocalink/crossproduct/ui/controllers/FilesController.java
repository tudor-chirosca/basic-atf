package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static java.lang.Long.parseLong;
import static java.util.Objects.nonNull;
import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import com.vocalink.crossproduct.infrastructure.exception.InvalidRequestParameterException;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FilesController implements FilesApi {

  public static final String REJECTED = "rejected";
  public static final String WILDCARD_REGEX = "^(\\*?)[a-zA-Z0-9_.]+(\\*?)$";

  private final FilesFacade filesFacade;

  @GetMapping(value = "/enquiry/files", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<FileDto>> getFiles(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final FileEnquirySearchRequest request) {

    if (request.getMessageDirection() == null) {
      throw new InvalidRequestParameterException("msg_direction is missing in request params");
    }

    if (nonNull(request.getCycleIds()) && !request.getCycleIds().isEmpty() &&
        nonNull(request.getDateTo())) {
      throw new InvalidRequestParameterException("cycle_ids and date_to are both included "
          + "in request params, exclude one of them");
    }

    if (nonNull(request.getSendingBic()) && nonNull(request.getReceivingBic()) &&
        request.getSendingBic().equals(request.getReceivingBic())) {
      throw new InvalidRequestParameterException("send_big and recv_bic are the same");
    }

    if (nonNull(request.getReasonCode()) && !REJECTED.equalsIgnoreCase(request.getStatus())) {
      throw new InvalidRequestParameterException("Have reason_code specified with missing "
          + "status value, or value that is not 'Rejected'");
    }

    if (nonNull(request.getDateFrom()) && request.getDateFrom()
        .isBefore(LocalDate.now().minusDays(parseLong(getDefault(DAYS_LIMIT))))) {
      throw new InvalidRequestParameterException("date_from can't be earlier than 30 days");
    }

    if (nonNull(request.getId()) && !request.getId().matches(WILDCARD_REGEX)) {
      throw new InvalidRequestParameterException("wildcard '*' can not be in the middle and "
          + "id can't contain special symbols beside '.' and '_'");
    }

    PageDto<FileDto> fileDto = filesFacade.getFiles(context, clientType, request);

    return ResponseEntity.ok().body(fileDto);
  }

  @GetMapping(value = "/enquiry/files/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FileDetailsDto> getFileDetails(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @PathVariable String fileId) {

    FileDetailsDto fileDetails = filesFacade.getDetailsById(context, clientType, fileId);

    return ResponseEntity.ok().body(fileDetails);
  }
}
