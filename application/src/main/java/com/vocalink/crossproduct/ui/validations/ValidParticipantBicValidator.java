package com.vocalink.crossproduct.ui.validations;

import static java.util.Objects.nonNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidParticipantBicValidator implements ConstraintValidator<ValidParticipantBic, String> {

  @Override
  public boolean isValid(String participantBic, ConstraintValidatorContext validatorContext) {

    return nonNull(participantBic) && !participantBic.isEmpty();
  }
}
