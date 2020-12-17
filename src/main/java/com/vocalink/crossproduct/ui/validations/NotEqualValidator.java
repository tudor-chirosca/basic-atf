package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.ui.validations.ValidatorUtils.tryGetFieldValue;
import static org.apache.commons.lang3.ObjectUtils.allNotNull;
import static org.apache.commons.lang3.ObjectUtils.notEqual;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEqualValidator implements
    ConstraintValidator<NotEqual, Object> {

  private String firstName;
  private String secondName;

  @Override
  public void initialize(NotEqual constraintAnnotation) {
    this.firstName = constraintAnnotation.first();
    this.secondName = constraintAnnotation.second();
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    String firstValue = tryGetFieldValue(obj, firstName);
    String secondValue = tryGetFieldValue(obj, secondName);

    if (allNotNull(firstValue, secondValue)) {
      return notEqual(firstValue, secondValue);
    }
    return true;
  }
}
