package com.vocalink.crossproduct.ui.validations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ValidChangeRequestValidator.class)
public @interface ValidChangeRequest {

  String message() default "Invalid change request";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
