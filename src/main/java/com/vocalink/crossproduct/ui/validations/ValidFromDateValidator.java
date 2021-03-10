package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static java.lang.Long.parseLong;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFromDateValidator implements ConstraintValidator<ValidFromDate, Temporal> {

  @Override
  public boolean isValid(Temporal value, ConstraintValidatorContext context) {

    return value == null || value.until(ZonedDateTime.now(), ChronoUnit.DAYS) <= parseLong(getDefault(DAYS_LIMIT));
  }
}
