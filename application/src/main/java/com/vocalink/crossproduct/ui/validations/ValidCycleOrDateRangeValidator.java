package com.vocalink.crossproduct.ui.validations;

import java.time.ZonedDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.vocalink.crossproduct.ui.validations.ValidatorUtils.tryGetFieldValue;

public class ValidCycleOrDateRangeValidator implements ConstraintValidator<ValidCycleOrDateRange, Object> {

    private String cycleIdFieldName;

    private String dateFromFieldName;

    private String dateToFieldName;

    @Override
    public void initialize(ValidCycleOrDateRange constraintAnnotation) {
        this.cycleIdFieldName = constraintAnnotation.cycleId();
        this.dateFromFieldName = constraintAnnotation.dateFrom();
        this.dateToFieldName = constraintAnnotation.dateTo();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        String cycleId = tryGetFieldValue(value, cycleIdFieldName);
        ZonedDateTime dateFrom = tryGetFieldValue(value, dateFromFieldName, ZonedDateTime.class);
        ZonedDateTime dateTo = tryGetFieldValue(value, dateToFieldName, ZonedDateTime.class);
        if (cycleId == null) {
            return dateFrom != null && dateTo != null;
        }
        return true;
    }
}
