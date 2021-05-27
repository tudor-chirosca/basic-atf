package com.vocalink.crossproduct.ui.aspects;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Positions {

  int POSITION_NOT_SET = -1;

  int context() default POSITION_NOT_SET;

  int clientType() default POSITION_NOT_SET;

  int content() default POSITION_NOT_SET;

  int request() default POSITION_NOT_SET;

  int requestId() default POSITION_NOT_SET;
}
