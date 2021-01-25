package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.approval.ApprovalDetails;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.facade.ApprovalFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalFacadeImpl implements ApprovalFacade {

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public ApprovalDetailsDto getApprovalDetailsById(String product, ClientType clientType,
      String id) {
    final ApprovalDetails approvalDetails = repositoryFactory.getApprovalClient(product).findByJobId(id);

    return presenterFactory.getPresenter(clientType).presentApprovalDetails(approvalDetails);
  }
}
