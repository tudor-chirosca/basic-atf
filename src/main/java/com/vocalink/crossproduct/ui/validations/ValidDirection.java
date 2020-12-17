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
@Constraint(validatedBy = ValidDirectionValidator.class)
@Documented
public @interface ValidDirection {

  String message() default "msg_direction in request parameters in empty or missing";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
