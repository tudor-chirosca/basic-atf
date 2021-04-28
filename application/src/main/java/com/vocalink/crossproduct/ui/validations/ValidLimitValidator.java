package com.vocalink.crossproduct.ui.validations;

import static java.util.Objects.nonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidLimitValidator implements ConstraintValidator<ValidLimit, Integer> {

  @Override
  public boolean isValid(Integer limit, ConstraintValidatorContext constraintValidatorContext) {
    return nonNull(limit) && limit > 0;
  }
}
