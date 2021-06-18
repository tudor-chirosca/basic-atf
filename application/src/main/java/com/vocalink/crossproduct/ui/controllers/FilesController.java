package com.vocalink.crossproduct.ui.controllers;

import java.io.IOException;

import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_DETAILS;
import static com.vocalink.crossproduct.ui.aspects.EventType.FILE_ENQUIRY;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.valueOf;

import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.config.ControllerFeatures;
import com.vocalink.crossproduct.ui.controllers.api.FilesApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    log.debug("Request: {}", request);

    final PageDto<FileDto> fileDto = filesFacade.getPaginated(context, clientType, request);

    return ResponseEntity.ok().body(fileDto);
  }

  @Auditable(type = FILE_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/files/{id}",
      produces = APPLICATION_OCTET_STREAM_VALUE)
  public void getFile(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

    final MediaType mediaType = valueOf(request.getHeader(ACCEPT_HEADER));

    if (mediaType.equals(MediaType.APPLICATION_OCTET_STREAM) &&
        !ControllerFeatures.FILE_DOWNLOAD.isActive()) {
      throw new ClientRequestException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
          "MediaType not supported: " + mediaType);
    }

    filesFacade.writeFileToOutputStream(context, clientType, id, response.getOutputStream());
    response.addHeader(CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
    response.addHeader(CONTENT_DISPOSITION, "attachment;filename=" + id);
    response.setStatus(HttpStatus.OK.value());
  }

  @Auditable(type = FILE_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/files/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getFileDetails(
          final @RequestHeader("client-type") ClientType clientType,
          final @RequestHeader String context,
          final @PathVariable String id, HttpServletRequest request) {

    final MediaType mediaType = valueOf(request.getHeader(ACCEPT_HEADER));

    if (!mediaType.isCompatibleWith(APPLICATION_JSON)) {
      throw new ClientRequestException(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
              "MediaType not supported: " + mediaType);
    }
    return ResponseEntity.ok(filesFacade.getDetailsById(context, clientType, id));
  }
}
