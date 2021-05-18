package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface ReferenceApi {

  @ApiOperation("Fetch all participant references")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Participant references retrieved successfully", response = ParticipantReferenceDto.class),
      @ApiResponse(code = 400, message = "Invalid context")
  })
  ResponseEntity<List<ParticipantReferenceDto>> getReferenceParticipants(
      ClientType clientType, String context);

  @ApiOperation("Get statuses for an enquiry type")
  @ApiResponses({
      @ApiResponse(code = 200, message = "File reference retrieved successfully", response = ReasonCodeReferenceDto.class),
      @ApiResponse(code = 500, message = "Invalid context")
  })
  ResponseEntity<List<ReasonCodeReferenceDto>> findFileReferencesByType(ClientType clientType, String context,
      String enquiryType);

  @ApiOperation("Fetch all message directions references")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Message directions references retrieved successfully", response = ParticipantReferenceDto.class),
      @ApiResponse(code = 400, message = "Invalid context")
  })
  ResponseEntity<List<MessageDirectionReferenceDto>> getMessageDirectionReferences(
      ClientType clientType, String context);

  @ApiOperation("Fetch cycles that were completed on a specific date")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Cycles by date retrieved successfully", response = CycleDto.class),
      @ApiResponse(code = 400, message = "Invalid context")
  })
  ResponseEntity<List<DayCycleDto>> getDayCyclesByDate(
      ClientType clientType, String context, String date, boolean settled);
}
