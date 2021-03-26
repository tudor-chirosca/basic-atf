package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AuditApi {

  @ApiOperation("Fetch audit and user details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "User details fetched successfully", response = UserDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<List<UserDetailsDto>> getUser(ClientType clientType, String context,
      String participantId);


  @ApiOperation("Fetch audit log")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Audit log fetched successfully", response = UserDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<Page<AuditDto>> getAuditLog(ClientType clientType, String context,
      @Validated AuditRequestParams parameters);
}