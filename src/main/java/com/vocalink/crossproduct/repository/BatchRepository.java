package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;

public interface BatchRepository {

  Page<Batch> findBatches(String context, BatchEnquirySearchRequest request);

}
