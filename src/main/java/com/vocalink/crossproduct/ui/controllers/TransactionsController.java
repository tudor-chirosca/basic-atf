package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.TransactionsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TransactionsController implements TransactionsApi {

  private final TransactionsFacade transactionsFacade;

  @PostMapping(value = "/enquiry/transactions/searches", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<TransactionDto>> getTransactions(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @RequestBody TransactionEnquirySearchRequest request) {

    final PageDto<TransactionDto> transactionsDto = transactionsFacade
        .getPaginated(context, clientType, request);

    return ResponseEntity.ok().body(transactionsDto);
  }
}
