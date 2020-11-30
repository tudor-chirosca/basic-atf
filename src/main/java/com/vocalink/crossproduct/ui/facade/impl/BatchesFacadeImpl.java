package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.repository.BatchRepository;
import com.vocalink.crossproduct.ui.dto.PageDto;
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

    final Page<Batch> batches = batchRepository.findBatches(context, request);

    return presenterFactory.getPresenter(clientType).presentBatches(batches);
  }
}
