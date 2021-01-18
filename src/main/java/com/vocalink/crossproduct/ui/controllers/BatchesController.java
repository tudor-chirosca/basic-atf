package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.BatchesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BatchesController implements BatchesApi {

  private final BatchesFacade batchesFacade;

  @GetMapping(value = "/enquiry/batches", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<BatchDto>> getBatches(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final BatchEnquirySearchRequest request) {

    final PageDto<BatchDto> batchesDto = batchesFacade.getPaginated(context, clientType, request);

    return ResponseEntity.ok().body(batchesDto);
  }

  @GetMapping(value = "/enquiry/batches/{batchId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BatchDetailsDto> getBatchDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String batchId) {

    final BatchDetailsDto batchDetailsDto = batchesFacade.getDetailsById(context, clientType, batchId);

    return ResponseEntity.ok().body(batchDetailsDto);
  }
}
