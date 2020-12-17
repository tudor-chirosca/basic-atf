package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.ui.validations.ValidatorUtils.tryGetFieldValue;
import static java.util.Arrays.asList;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidStatusValidator implements
    ConstraintValidator<ValidStatus, Object> {

  private String statusFieldName;
  private String reasonCodeFieldName;
  private List<String> statuses;

  @Override
  public void initialize(ValidStatus constraintAnnotation) {
    this.statusFieldName = constraintAnnotation.status();
    this.reasonCodeFieldName = constraintAnnotation.reasonCode();
    this.statuses = asList(constraintAnnotation.statuses());
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    String status = tryGetFieldValue(obj, statusFieldName, String.class);
    String reasonCode = tryGetFieldValue(obj, reasonCodeFieldName, String.class);

    if (reasonCode == null) {
      return true;
    }

    return statuses.stream().anyMatch( st -> st.equalsIgnoreCase(status));
  }
}
