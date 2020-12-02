package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.batch.BatchesClient;
import com.vocalink.crossproduct.shared.batch.CPBatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import java.util.List;
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
  public List<Batch> findBatchesByIds(String context, List<String> batchIds) {
    final BatchesClient batchClient = clientFactory.getBatchesClient(context);

    log.info("Fetching batches with ids: {}, from: {}", batchIds, context);

    return batchClient.findBatchesByIds(batchIds)
        .getItems()
        .stream()
        .map(MAPPER::toEntity)
        .collect(toList());
  }
}
