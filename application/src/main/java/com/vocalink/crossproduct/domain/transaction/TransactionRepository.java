package com.vocalink.crossproduct.domain.transaction;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface TransactionRepository extends CrossproductRepository {

  Page<Transaction> findPaginated(TransactionEnquirySearchCriteria criteria);

  Transaction findById(String id);
}
