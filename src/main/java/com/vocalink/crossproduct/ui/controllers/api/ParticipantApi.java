package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface ParticipantApi {

  @ApiOperation("Fetch all managed participants")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Managed participants retrieved successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<ManagedParticipantDto>> getParticipants(final ClientType clientType,
      final String context, final ManagedParticipantsSearchRequest request);

  @ApiOperation("Fetch managed participant details records")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Managed participant details retrieved successfully", response = ManagedParticipantDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ManagedParticipantDetailsDto> getManagedParticipantDetails(final ClientType clientType,
      final String context, final String bic);
}
