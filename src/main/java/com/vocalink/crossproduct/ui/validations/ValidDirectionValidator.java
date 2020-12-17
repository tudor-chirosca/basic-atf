package com.vocalink.crossproduct.ui.validations;

import static java.util.Objects.nonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDirectionValidator implements ConstraintValidator<ValidDirection, String> {

  @Override
  public boolean isValid(String direction, ConstraintValidatorContext validatorContext) {

    return nonNull(direction) && !direction.isEmpty();
  }
}
