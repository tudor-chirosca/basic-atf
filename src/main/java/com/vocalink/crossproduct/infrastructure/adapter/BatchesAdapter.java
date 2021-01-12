package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.batch.BatchesClient;
import com.vocalink.crossproduct.shared.batch.CPBatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchesAdapter implements BatchRepository {

  private final ClientFactory clientFactory;

  @Override
  public Page<Batch> findBatchesPaginated(String context, BatchEnquirySearchRequest request) {

    final BatchesClient batchClient = clientFactory.getBatchesClient(context);
    log.info("Fetching all batches from: {}", context);

    CPBatchEnquirySearchRequest cpRequest = MAPPER.toCp(request);

    return MAPPER.toEntityBatch(batchClient.findBatches(cpRequest));
  }

  @Override
  public Batch findBatchById(String context, String id) {
    final BatchesClient batchClient = clientFactory.getBatchesClient(context);

    log.info("Fetching batch with id: {}, from: {}", id, context);

    return MAPPER.toEntity(batchClient.findBatchById(id));
  }
}
