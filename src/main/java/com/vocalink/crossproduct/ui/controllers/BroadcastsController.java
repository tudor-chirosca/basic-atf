package com.vocalink.crossproduct.ui.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vocalink.crossproduct.ui.controllers.api.BroadcastsApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.facade.api.BroadcastsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BroadcastsController implements BroadcastsApi {

  private final BroadcastsFacade broadcastsFacade;

  @GetMapping(value = "/broadcasts", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<BroadcastDto>> getPaginatedBroadcasts(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final BroadcastsSearchParameters searchParameters) {
    log.debug("Request parameters: {}", searchParameters);

    final PageDto<BroadcastDto> paginated = broadcastsFacade
        .getPaginated(context, clientType, searchParameters);

    return ResponseEntity.ok(paginated);
  }

  @PostMapping(value = "/broadcasts", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<BroadcastDto> sendBroadcast(final ClientType clientType,
      final String context,
      final @RequestBody BroadcastRequest request) {
    log.debug("Request: {}", request);

    final BroadcastDto broadcast = broadcastsFacade.create(context, clientType, request.getMessage(), request.getRecipients());

    return ResponseEntity.ok(broadcast);
  }
}
