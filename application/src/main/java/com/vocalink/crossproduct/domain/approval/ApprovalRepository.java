package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import java.util.List;

public interface ApprovalRepository extends CrossproductRepository {

  Approval findByJobId(String jobId);

  Page<Approval> findPaginated(ApprovalSearchCriteria criteria);

  ApprovalCreationResponse requestApproval(ApprovalChangeCriteria criteria, String userId);

  List<UserDetails> findRequestedDetails();

  List<ApprovalRequestType> findApprovalRequestTypes();

}
