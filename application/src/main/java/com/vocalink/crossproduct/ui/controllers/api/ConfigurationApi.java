package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.domain.configuration.Configuration;
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;


public interface ConfigurationApi {

  @ApiOperation("Get product specific configuration")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Got product specific configuration", response = Configuration.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ConfigurationDto> getConfiguration(final ClientType clientType,
      final String context);
}
