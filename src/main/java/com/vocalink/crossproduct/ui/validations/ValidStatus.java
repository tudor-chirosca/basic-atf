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
@Constraint(validatedBy = ValidStatusValidator.class)
@Documented
public @interface ValidStatus {

  String status();

  String reasonCode();

  String[] statuses();

  String message() default "Reason code should not be any of the rejected types";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
