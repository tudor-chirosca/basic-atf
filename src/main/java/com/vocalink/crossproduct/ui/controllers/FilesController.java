package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_DETAILS;
import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_ENQUIRY;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.valueOf;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.FilesApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FilesController implements FilesApi {

  public static final String ACCEPT_HEADER = "Accept";
  private final FilesFacade filesFacade;

  @Auditable(type = FILE_ENQUIRY, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/files", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<FileDto>> getFiles(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final FileEnquirySearchRequest request, HttpServletRequest servletRequest) {

    final PageDto<FileDto> fileDto = filesFacade.getPaginated(context, clientType, request);

    return ResponseEntity.ok().body(fileDto);
  }

  @Auditable(type = FILE_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/files/{id}",
      produces = {APPLICATION_JSON_VALUE, APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> getFile(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String id, HttpServletRequest request) {

    final MediaType mediaType = valueOf(request.getHeader(ACCEPT_HEADER));

    if (mediaType.isCompatibleWith(APPLICATION_JSON)) {
      return ResponseEntity.ok(filesFacade.getDetailsById(context, clientType, id));
    }

    Resource resource = filesFacade.getFile(context, clientType, id);

    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
    httpHeaders.add(CONTENT_DISPOSITION, "attachment;filename=" + id);

    return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
  }
}
