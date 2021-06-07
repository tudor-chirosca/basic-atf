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
@Constraint(validatedBy = ValidParticipantBicValidator.class)
@Documented
public @interface ValidParticipantBic {

  String message() default "Participant bic in request is empty or missing";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
