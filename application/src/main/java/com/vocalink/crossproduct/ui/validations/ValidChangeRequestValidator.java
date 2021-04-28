package com.vocalink.crossproduct.ui.validations;

import java.util.Map;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidChangeRequestValidator implements
    ConstraintValidator<ValidChangeRequest, Map<String, Object>> {

  public static final String DEBIT_CAP_LIMIT = "debitCapLimit";
  public static final String VALUE_BETWEEN_1_AND_99_BILLION = "Please enter a value between 1 and 99 billion";
  public static final String NUMBER_SHOULD_NOT_BE_DECIMAL = "Inserted number should not be Decimal";

  @Override
  public boolean isValid(Map<String, Object> model, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    Object object = model.get(DEBIT_CAP_LIMIT);
    if (Objects.nonNull(object)) {
      return isValidDebitCap(object, context);
    }
    return true;
  }

  private boolean isValidDebitCap(Object debitCap, ConstraintValidatorContext context) {
    if (debitCap instanceof Long) {
      customMessageForValidation(context, VALUE_BETWEEN_1_AND_99_BILLION);
      Long longDebitCap = (Long) debitCap;
      return longDebitCap <= 99000000000L;
    } else if (debitCap instanceof Integer) {
      customMessageForValidation(context, VALUE_BETWEEN_1_AND_99_BILLION);
      Integer integerDebitCap = (Integer) debitCap;
      return integerDebitCap > 0;
    }
    customMessageForValidation(context, NUMBER_SHOULD_NOT_BE_DECIMAL);
    return false;
  }

  private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
    constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }
}
