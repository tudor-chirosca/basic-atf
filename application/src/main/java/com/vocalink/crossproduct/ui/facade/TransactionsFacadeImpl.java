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
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionsFacadeImpl implements TransactionsFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;
  private final Clock clock;
  private final String zoneId;

  @Autowired
  public TransactionsFacadeImpl(
      PresenterFactory presenterFactory,
      RepositoryFactory repositoryFactory,
      Clock clock,
      @Value("${app.ui.config.default.timeZone}") String zoneId) {
    this.presenterFactory = presenterFactory;
    this.repositoryFactory = repositoryFactory;
    this.clock = clock;
    this.zoneId = zoneId;
  }

  @Override
  public PageDto<TransactionDto> getPaginated(String product, ClientType clientType,
      TransactionEnquirySearchRequest requestDto) {
    log.info("Fetching transactions for: {} from: {}", clientType, product);

    final ZonedDateTime valueDateUTC = convertToUTC(requestDto.getValueDate());
    final TransactionEnquirySearchCriteria request = MAPPER.toEntity(requestDto, valueDateUTC);

    final Page<Transaction> page = repositoryFactory.getTransactionRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType)
        .presentTransactions(page.getTotalResults(), page.getItems());
  }

  @Override
  public TransactionDetailsDto getDetailsById(String product, ClientType clientType, String id) {
    log.info("Fetching transaction details for id: {} for: {} from: {}", id, clientType, product);

    final Transaction transaction = repositoryFactory.getTransactionRepository(product)
        .findById(id);

    return presenterFactory.getPresenter(clientType).presentTransactionDetails(transaction);
  }

  protected ZonedDateTime convertToUTC(ZonedDateTime valueDate) {
    if (valueDate == null) {
      return null;
    }
    final ZonedDateTime pseudo = ZonedDateTime.of(valueDate.toLocalDateTime(), ZoneId.of(zoneId));
    return pseudo.withZoneSameInstant(clock.getZone());
  }
}
