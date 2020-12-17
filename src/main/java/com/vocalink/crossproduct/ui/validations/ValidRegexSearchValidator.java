package com.vocalink.crossproduct.ui.validations;

import static java.util.Objects.isNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidRegexSearchValidator implements
    ConstraintValidator<ValidRegexSearch, String> {

  private String regExp;

  @Override
  public void initialize(ValidRegexSearch constraintAnnotation) {
    this.regExp = constraintAnnotation.regExp();
  }

  @Override
  public boolean isValid(String id,
      ConstraintValidatorContext validatorContext) {
    return isNull(id) || id.matches(regExp);
  }
}
