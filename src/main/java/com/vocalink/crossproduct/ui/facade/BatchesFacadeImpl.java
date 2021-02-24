package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Result;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.api.BatchesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchesFacadeImpl implements BatchesFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public PageDto<BatchDto> getPaginated(String product, ClientType clientType,
      BatchEnquirySearchRequest requestDto) {
    log.info("Fetching batches from: {}", product);

    final BatchEnquirySearchCriteria request = MAPPER.toEntity(requestDto);
    final Result<Batch> result = repositoryFactory.getBatchRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType)
        .presentBatches(result.getSummary().getTotalCount(), result.getData());
  }

  @Override
  public BatchDetailsDto getDetailsById(String product, ClientType clientType, String id) {
    log.info("Fetching batch details for id: {} from: {}", id, product);

    final Batch batch = repositoryFactory.getBatchRepository(product).findById(id);
    final Participant sender = repositoryFactory.getParticipantRepository(product)
        .findById(batch.getSender());
    final File file = repositoryFactory.getFileRepository(product).findById(batch.getFileName());

    return presenterFactory.getPresenter(clientType).presentBatchDetails(batch, sender, file);
  }
}
