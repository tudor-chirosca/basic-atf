package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.controllers.api.ConfigurationApi;
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.facade.api.ConfigurationFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class ConfigurationController implements ConfigurationApi {

  private final ConfigurationFacade configurationFacade;

  @GetMapping(value = "/configuration", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ConfigurationDto> getConfiguration(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context) {
    ConfigurationDto configurationDto = configurationFacade
        .getConfiguration(context, clientType);

    return ResponseEntity.ok().body(configurationDto);
  }
}
