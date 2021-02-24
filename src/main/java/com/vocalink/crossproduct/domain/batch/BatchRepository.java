package com.vocalink.crossproduct.domain.batch;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Result;

public interface BatchRepository extends CrossproductRepository {

  Result<Batch> findPaginated(BatchEnquirySearchCriteria criteria);

  Batch findById(String id);

}
