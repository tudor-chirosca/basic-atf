package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.Collections.singletonList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchRepository;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.facade.BatchesFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BatchesFacadeImpl implements BatchesFacade {

  private final PresenterFactory presenterFactory;
  private final BatchRepository batchRepository;
  
  @Override
  public PageDto<BatchDto> getBatches(String context, ClientType clientType,
      BatchEnquirySearchRequest request) {

    final Page<Batch> batches = batchRepository.findBatchesPaginated(context, request);

    return presenterFactory.getPresenter(clientType).presentBatches(batches);
  }

  @Override
  public BatchDetailsDto getDetailsById(String context, ClientType clientType, String id) {

    final Batch batch = batchRepository
        .findBatchesByIds(context, singletonList(id))
        .stream()
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException("There is no batch with such an id"));

    return presenterFactory.getPresenter(clientType).presentBatchDetails(batch);
  }
}
