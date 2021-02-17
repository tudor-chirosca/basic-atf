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
@Constraint(validatedBy = ValidSortValidator.class)
@Documented
public @interface ValidSort {

  String sort();

  String[] sortingKeys();

  String message() default "Wrong sorting parameter";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
