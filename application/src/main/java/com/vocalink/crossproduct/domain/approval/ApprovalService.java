package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.domain.CrossproductService;

public interface ApprovalService extends CrossproductService {

  ApprovalConfirmationResponse submitApprovalConfirmation(
      ApprovalConfirmation approvalConfirmation);

}
