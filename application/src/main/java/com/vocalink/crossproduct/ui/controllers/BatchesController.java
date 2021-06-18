package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.BATCH_DETAILS;
import static com.vocalink.crossproduct.ui.aspects.EventType.BATCH_ENQUIRY;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.BatchesApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.BatchesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
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
public class BatchesController implements BatchesApi {

  private final BatchesFacade batchesFacade;

  @Auditable(type = BATCH_ENQUIRY, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/batches", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<BatchDto>> getBatches(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final BatchEnquirySearchRequest searchRequest,
      final HttpServletRequest request) {
    log.debug("Request: {}", searchRequest);

    final PageDto<BatchDto> batchesDto = batchesFacade.getPaginated(context, clientType, searchRequest);

    return ResponseEntity.ok().body(batchesDto);
  }

  @Auditable(type = BATCH_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/batches/{batchId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BatchDetailsDto> getBatchDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String batchId,
      final HttpServletRequest request) {

    final BatchDetailsDto batchDetailsDto = batchesFacade
        .getDetailsById(context, clientType, batchId);

    return ResponseEntity.ok().body(batchDetailsDto);
  }
}
