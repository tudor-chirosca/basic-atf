package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;


public interface ReferenceApi {

  @ApiOperation("Fetch all reference participants")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Reference participants retrieved successfully", response = ParticipantReferenceDto.class),
      @ApiResponse(code = 400, message = "Invalid context")
  })
  ResponseEntity<List<ParticipantReferenceDto>> getReferenceParticipants(
      ClientType clientType, String context);

  @ApiOperation("Fetch all file references")
  @ApiResponses({
      @ApiResponse(code = 200, message = "File reference retrieved successfully", response = FileStatusesDto.class),
      @ApiResponse(code = 500, message = "Invalid context")
  })
  ResponseEntity<List<FileStatusesDto>> getFileReferences(
      ClientType clientType, String context);
}
