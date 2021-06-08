package com.vocalink.crossproduct.ui.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ValidCycleOrDateRangeValidator.class})
@Documented
public @interface ValidCycleOrDateRange {

    String cycleId();

    String dateFrom();

    String dateTo();

    String message() default "CycleId either both dateFrom and dateTo must not be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
