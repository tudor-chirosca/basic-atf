package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserApi {

  @ApiOperation("Get information about currently logged in user and the participant he belongs to")
  @ApiResponses({
      @ApiResponse(code = 200, message = "User information retrieved successfully", response = CurrentUserInfoDto.class)
  })
  ResponseEntity<CurrentUserInfoDto> getCurrentUserInfo(ClientType clientType, String context,
      HttpServletRequest request);
}
