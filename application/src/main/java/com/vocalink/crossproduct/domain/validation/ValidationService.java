package com.vocalink.crossproduct.domain.validation;

import com.vocalink.crossproduct.domain.CrossproductService;
import java.time.ZonedDateTime;

public interface ValidationService  extends CrossproductService {

  ValidationApproval getApprovalValidation(ZonedDateTime dateTime);
}
