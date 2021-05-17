package com.vocalink.crossproduct.ui.controllers.api;

import java.io.IOException;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface FilesApi {

  @ApiOperation("Fetch Files")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Files fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<FileDto>> getFiles(
      final ClientType clientType,
      final String context,
      @Valid final FileEnquirySearchRequest request, HttpServletRequest servletRequest
  );

  @ApiOperation("Fetch File")
  @ApiResponses({
          @ApiResponse(code = 200, message = "File fetched successfully", response = FileDetailsDto.class),
          @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  void getFile(
          final ClientType clientType,
          final String context,
          final String id, HttpServletRequest request, HttpServletResponse response) throws IOException;

  @ApiOperation("Fetch File Details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "File details fetched successfully", response = FileDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<?> getFileDetails(
      final ClientType clientType,
      final String context,
      final String id, HttpServletRequest request);
}
