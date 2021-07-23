package com.vocalink.crossproduct.ui.validations;

import static com.vocalink.crossproduct.ui.validations.ValidatorUtils.tryGetFieldValue;
import static java.util.Arrays.asList;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidNoteValidator implements
    ConstraintValidator<ValidNote, Object> {

  private String noteFieldName;
  private String requestTypeFieldName;
  private List<String> requestTypes;

  @Override
  public void initialize(ValidNote constraintAnnotation) {
    this.noteFieldName = constraintAnnotation.notes();
    this.requestTypeFieldName = constraintAnnotation.requestType();
    this.requestTypes = asList(constraintAnnotation.requestTypes());
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context) {
    String requestType = tryGetFieldValue(obj, requestTypeFieldName);
    String notes = tryGetFieldValue(obj, noteFieldName);

    if (notes == null) {
      return requestTypes.stream().noneMatch(rt -> rt.equalsIgnoreCase(requestType));
    }

    if (notes.length() > 1000) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("Notes parameter should not exceed 1000 symbols")
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
