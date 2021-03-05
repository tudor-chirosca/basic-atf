package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Result;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.TransactionsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.Objects;
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
    final Result<Transaction> result = repositoryFactory.getTransactionRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType)
        .presentTransactions(result.getSummary().getTotalCount(), result.getData());
  }

  @Override
  public TransactionDetailsDto getDetailsById(String product, ClientType clientType, String id) {
    log.info("Fetching transaction details for id: {} from: {}", id, product);

    final Transaction transaction = repositoryFactory.getTransactionRepository(product)
        .findById(id);
    final Account senderAccount = repositoryFactory.getAccountRepository(product)
        .findByPartyCode(transaction.getSenderParticipantIdentifier());
    final Participant senderParticipant = repositoryFactory.getParticipantRepository(product)
        .findById(transaction.getSenderParticipantIdentifier());
    final EnquirySenderDetails sender = MAPPER.toEntity(senderAccount, senderParticipant);

    if (Objects.nonNull(transaction.getReceiverParticipantIdentifier())) {
      final Account receiverAccount = repositoryFactory.getAccountRepository(product)
          .findByPartyCode(transaction.getReceiverParticipantIdentifier());
      final Participant receiverParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(transaction.getReceiverParticipantIdentifier());
      final EnquirySenderDetails receiver = MAPPER.toEntity(receiverAccount, receiverParticipant);

      return presenterFactory.getPresenter(clientType)
          .presentTransactionDetails(transaction, sender, receiver);
    }

    return presenterFactory.getPresenter(clientType).presentTransactionDetails(transaction, sender);
  }
}
