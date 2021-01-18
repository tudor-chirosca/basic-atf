package com.vocalink.crossproduct.ui.facade.impl;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.TransactionsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionsFacadeImpl implements TransactionsFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public PageDto<TransactionDto> getPaginated(String product, ClientType clientType,
      TransactionEnquirySearchRequest requestDto) {

    final TransactionEnquirySearchCriteria request = MAPPER.toEntity(requestDto);
    final Page<Transaction> transactions = repositoryFactory.getTransactionClient(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType).presentTransactions(transactions);
  }
}
