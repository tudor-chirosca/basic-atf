package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.FilesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
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

  private final FilesFacade filesFacade;

  @GetMapping(value = "/enquiry/files", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<FileDto>> getFiles(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final FileEnquirySearchRequest request) {

    PageDto<FileDto> fileDto = filesFacade.getFiles(context, clientType, request);

    return ResponseEntity.ok().body(fileDto);
  }

  @GetMapping(value = "/enquiry/files/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FileDetailsDto> getFileDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String fileId) {

    FileDetailsDto fileDetails = filesFacade.getDetailsById(context, clientType, fileId);

    return ResponseEntity.ok().body(fileDetails);
  }
}
