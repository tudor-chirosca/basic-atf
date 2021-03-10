package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ServiceFactory;
import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.facade.api.ValidationFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationFacadeImpl implements ValidationFacade {

  private final PresenterFactory presenterFactory;
  private final ServiceFactory serviceFactory;

  @Override
  public ValidationApprovalDto getApprovalValidation(String product, ClientType clientType,
      ZonedDateTime currentTime) {

    final ValidationApproval approvalValidation = serviceFactory.getValidationService(product)
        .getApprovalValidation(currentTime);

    return presenterFactory.getPresenter(clientType).presentApprovalValidation(approvalValidation);
  }
}
