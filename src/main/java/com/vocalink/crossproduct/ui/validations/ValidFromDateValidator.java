package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static java.lang.Long.parseLong;
import static java.util.Objects.nonNull;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFromDateValidator implements
    ConstraintValidator<ValidFromDate, LocalDate> {

  @Override
  public boolean isValid(LocalDate date,
      ConstraintValidatorContext validatorContext) {

    if (nonNull(date)) {
      return !date.isBefore(LocalDate.now().minusDays(parseLong(getDefault(DAYS_LIMIT))));
    }
    return true;
  }
}
