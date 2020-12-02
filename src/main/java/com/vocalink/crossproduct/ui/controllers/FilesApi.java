package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface FilesApi {

  @ApiOperation("Fetch Files")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Files fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<FileDto>> getFiles(
      final ClientType clientType,
      final String context,
      final FileEnquirySearchRequest request
  );

  @ApiOperation("Fetch File Details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "File details fetched successfully", response = FileDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<FileDetailsDto> getFileDetails(
      final ClientType clientType,
      final String context,
      final String fileId);
}
