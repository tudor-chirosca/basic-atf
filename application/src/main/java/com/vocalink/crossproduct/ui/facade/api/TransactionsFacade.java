package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface TransactionsFacade {

  PageDto<TransactionDto> getPaginated(String product, ClientType clientType,
      TransactionEnquirySearchRequest request);

  TransactionDetailsDto getDetailsById(String product, ClientType clientType, String id);
}
