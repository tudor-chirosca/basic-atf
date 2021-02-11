package com.vocalink.crossproduct.ui.validations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidNoteValidator.class)
@Documented
public @interface ValidNote {

  String notes();

  String requestType();

  String[] requestTypes();

  String message() default "Notes parameter is mandatory on a BATCH_CANCELLATION";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
