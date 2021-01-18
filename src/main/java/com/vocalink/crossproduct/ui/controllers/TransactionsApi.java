package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
      final ClientType clientType,
      final String context,
      @Valid final TransactionEnquirySearchRequest request
  );
}
