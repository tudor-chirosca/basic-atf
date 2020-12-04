package com.vocalink.crossproduct.domain.batch;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import java.util.List;

public interface BatchRepository {

  Page<Batch> findBatchesPaginated(String context, BatchEnquirySearchRequest request);

  Batch findBatchById(String context, String id);

}
