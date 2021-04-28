package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface ValidationApi {

  @ApiOperation("Validate whenever an approval operation can be executed")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Validation result retrieved successfully", response = ValidationApprovalDto.class)
  })
  ResponseEntity<ValidationApprovalDto> getApprovalValidation(final ClientType clientType,
      final String context);
}
