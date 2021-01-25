package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.domain.CrossproductRepository;

public interface ApprovalRepository extends CrossproductRepository {

  ApprovalDetails findByJobId(String jobId);
}
