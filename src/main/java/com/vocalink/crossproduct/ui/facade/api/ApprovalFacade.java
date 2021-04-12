package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;

public interface ApprovalFacade {

  ApprovalDetailsDto getApprovalDetailsById(String product, ClientType clientType, String id);

  PageDto<ApprovalDetailsDto> getApprovals(String product, ClientType clientType,
      ApprovalSearchRequest request);

  ApprovalDetailsDto requestApproval(String product, ClientType clientType,
      ApprovalChangeRequest request);

  ApprovalConfirmationResponseDto submitApprovalConfirmation(String product, ClientType clientType,
      ApprovalConfirmationRequest request, String id);

  List<ApprovalUserDto> findRequestedDetails(String product, ClientType clientType);

  List<ApprovalRequestType> findApprovalRequestTypes(String product, ClientType clientType);

}
