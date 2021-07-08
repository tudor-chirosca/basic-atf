package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.domain.CrossproductService;
import java.util.List;

public interface ApprovalService extends CrossproductService {

  ApprovalConfirmationResponse submitApprovalConfirmation(
      ApprovalConfirmation approvalConfirmation);

  List<ApprovalRequestType> getRequestTypes();
}
