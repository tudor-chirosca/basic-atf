package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface ApprovalFacade {

  ApprovalDetailsDto getApprovalDetailsById(String product, ClientType clientType, String id);

  PageDto<ApprovalDetailsDto> getApprovals(String product, ClientType clientType,
      ApprovalSearchRequest request);

  ApprovalDetailsDto requestApproval(String product, ClientType clientType,
      ApprovalChangeRequest request);
}
