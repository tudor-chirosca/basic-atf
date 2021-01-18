package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface TransactionsFacade {

  PageDto<TransactionDto> getPaginated(String product, ClientType clientType,
      TransactionEnquirySearchRequest request);
}
