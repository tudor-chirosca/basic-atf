package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface ApprovalRepository extends CrossproductRepository {

  Approval findByJobId(String jobId);

  Page<Approval> findPaginated(ApprovalSearchCriteria criteria);

  Approval requestApproval(ApprovalChangeCriteria criteria);

}
