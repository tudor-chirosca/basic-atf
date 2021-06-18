package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ServiceFactory;
import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.facade.api.ValidationFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationFacadeImpl implements ValidationFacade {

  private final PresenterFactory presenterFactory;
  private final ServiceFactory serviceFactory;

  @Override
  public ValidationApprovalDto getApprovalValidation(String product, ClientType clientType,
      ZonedDateTime currentTime) {
    log.info("Fetching approval validation by date: {} for: {} from: {}", currentTime, clientType, product);

    final ValidationApproval approvalValidation = serviceFactory.getValidationService(product)
        .getApprovalValidation(currentTime);

    return presenterFactory.getPresenter(clientType).presentApprovalValidation(approvalValidation);
  }
}
