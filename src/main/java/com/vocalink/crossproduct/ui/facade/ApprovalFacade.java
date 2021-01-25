package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface ApprovalFacade {

  ApprovalDetailsDto getApprovalDetailsById(String context, ClientType clientType, String id);
}
