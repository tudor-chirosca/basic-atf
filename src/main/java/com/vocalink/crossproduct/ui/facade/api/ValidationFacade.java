package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.ZonedDateTime;

public interface ValidationFacade {

  ValidationApprovalDto getApprovalValidation(String product, ClientType clientType,
      ZonedDateTime currentTime);
}
