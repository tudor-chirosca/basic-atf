package com.vocalink.crossproduct.domain.batch;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface BatchRepository extends CrossproductRepository {

  Page<Batch> findPaginated(BatchEnquirySearchCriteria criteria);

  Batch findById(String id);

}
