package com.vocalink.crossproduct.ui.facade.impl;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
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
  private final RepositoryFactory repositoryFactory;

  @Override
  public PageDto<BatchDto> getPaginated(String product, ClientType clientType,
      BatchEnquirySearchRequest requestDto) {

    final BatchEnquirySearchCriteria request = MAPPER.toEntity(requestDto);
    final Page<Batch> batches = repositoryFactory.getBatchRepository(product)
        .findPaginated(request);

    return presenterFactory.getPresenter(clientType).presentBatches(batches);
  }

  @Override
  public BatchDetailsDto getDetailsById(String product, ClientType clientType, String id) {

    final Batch batch = repositoryFactory.getBatchRepository(product).findById(id);
    
    return presenterFactory.getPresenter(clientType).presentBatchDetails(batch);
  }
}
