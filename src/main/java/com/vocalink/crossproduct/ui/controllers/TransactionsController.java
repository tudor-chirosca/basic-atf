package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.TRANSACTION_DETAILS;
import static com.vocalink.crossproduct.ui.aspects.EventType.TRANSACTION_ENQUIRY;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.TransactionsApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.TransactionsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TransactionsController implements TransactionsApi {

  private final TransactionsFacade transactionsFacade;

  @Auditable(type = TRANSACTION_ENQUIRY, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @PostMapping(value = "/enquiry/transactions/searches", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<TransactionDto>> getTransactions(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @RequestBody TransactionEnquirySearchRequest searchRequest,
      final HttpServletRequest request) {

    final PageDto<TransactionDto> transactionsDto = transactionsFacade
        .getPaginated(context, clientType, searchRequest);

    return ResponseEntity.ok().body(transactionsDto);
  }

  @Auditable(type = TRANSACTION_DETAILS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/enquiry/transactions/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionDetailsDto> getTransactionDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String transactionId,
      final HttpServletRequest request) {

    final TransactionDetailsDto transactionDetailsDto = transactionsFacade
        .getDetailsById(context, clientType, transactionId);

    return ResponseEntity.ok().body(transactionDetailsDto);
  }
}
