package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.domain.files.FileEnquiry;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquiryDto;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface FileEnquiriesApi {

  @ApiOperation("Fetch File Enquiries")
  @ApiResponses({
      @ApiResponse(code = 200, message = "File Enquiries fetched successfully", response = AlertReferenceDataDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<FileEnquiryDto>> getFileEnquiries(
      final ClientType clientType,
      final String context,
      final FileEnquirySearchRequest request
  );
}
