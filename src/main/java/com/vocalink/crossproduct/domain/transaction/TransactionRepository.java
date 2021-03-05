package com.vocalink.crossproduct.domain.transaction;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Result;

public interface TransactionRepository extends CrossproductRepository {

  Result<Transaction> findPaginated(TransactionEnquirySearchCriteria criteria);

  Transaction findById(String id);
}
