package com.vocalink.crossproduct.ui.validations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, CharSequence> {
  private List<String> acceptedValues;

  @Override
  public void initialize(ValidEnum annotation) {
    acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    return acceptedValues.contains(value.toString());
  }
}