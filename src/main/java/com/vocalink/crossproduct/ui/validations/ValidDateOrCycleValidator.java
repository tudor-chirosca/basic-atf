package com.vocalink.crossproduct.ui.validations;


import java.lang.reflect.Field;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidDateOrCycleValidator implements ConstraintValidator<ValidDateOrCycle, Object> {

  private String dateFieldName;
  private String cycleFiledName;

  @Override
  public void initialize(ValidDateOrCycle constraintAnnotation) {
    this.dateFieldName = constraintAnnotation.date();
    this.cycleFiledName = constraintAnnotation.cycles();
  }

  @Override
  public boolean isValid(Object obj,
      ConstraintValidatorContext validatorContext) {

    LocalDate date = tryGetFieldValue(obj, dateFieldName, LocalDate.class);
    String cycles = tryGetFieldValue(obj, cycleFiledName, String.class);

    return cycles == null || cycles.isEmpty() || date == null;
  }

  private <T> T tryGetFieldValue(Object obj, String name, Class<T> type) {
    try {
      final Field field = obj.getClass().getDeclaredField(name);
      field.setAccessible(true);

      return type.cast(field.get(obj));
    } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
      return null;
    }
  }
}
