package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface TransactionsApi {

  @ApiOperation("Fetch Transactions")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Transactions fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<TransactionDto>> getTransactions(
      ClientType clientType,
      String context,
      @Valid TransactionEnquirySearchRequest searchRequest,
      HttpServletRequest request
  );

  @ApiOperation("Fetch Transaction Details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Transaction details fetched successfully", response = TransactionDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<TransactionDetailsDto> getTransactionDetails(
      ClientType clientType,
      String context,
      String transactionId,
      HttpServletRequest request);

}
