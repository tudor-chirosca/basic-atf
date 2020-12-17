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
@Constraint(validatedBy = ValidDateOrCycleValidator.class)
@Documented
public @interface ValidDateOrCycle {

  String date();

  String cycles();

  String message() default "cycle_ids and date_to are both included in request params, exclude one of them";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
