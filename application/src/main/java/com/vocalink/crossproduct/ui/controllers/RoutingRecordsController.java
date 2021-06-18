package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest;
import com.vocalink.crossproduct.ui.facade.api.RoutingRecordFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoutingRecordsController {

  private final RoutingRecordFacade routingRecordFacade;

  @GetMapping(value = "/participants/{bic}/routing", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<RoutingRecordDto>> getRoutingRecords(
      @RequestHeader("client-type") ClientType clientType,
      @RequestHeader String context,
      @PathVariable String bic,
      RoutingRecordRequest request) {
    log.debug("Request: {}", request);

    final PageDto<RoutingRecordDto> routingRecordsDto = routingRecordFacade
        .getPaginated(context, clientType, request, bic);

    return ResponseEntity.ok().body(routingRecordsDto);
  }
}
