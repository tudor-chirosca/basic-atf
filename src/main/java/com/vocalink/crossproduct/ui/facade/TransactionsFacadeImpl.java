package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.TransactionsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionsFacadeImpl implements TransactionsFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public PageDto<TransactionDto> getPaginated(String product, ClientType clientType,
      TransactionEnquirySearchRequest requestDto) {
    log.info("Fetching transactions from: {}", product);

    final TransactionEnquirySearchCriteria request = MAPPER.toEntity(requestDto);
    final Page<Transaction> page = repositoryFactory.getTransactionRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType)
        .presentTransactions(page.getTotalResults(), page.getItems());
  }

  @Override
  public TransactionDetailsDto getDetailsById(String product, ClientType clientType, String id) {
    log.info("Fetching transaction details for id: {} from: {}", id, product);

    final Transaction transaction = repositoryFactory.getTransactionRepository(product)
        .findById(id);

    return presenterFactory.getPresenter(clientType).presentTransactionDetails(transaction);
  }
}
