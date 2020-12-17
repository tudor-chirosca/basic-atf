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
@Constraint(validatedBy = ValidRegexSearchValidator.class)
@Documented
public @interface ValidRegexSearch {

  String regExp();

  String message() default "wildcard '*' can not be in the middle and id should not contain special symbols beside '.' and '_'";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
