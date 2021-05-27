package com.vocalink.crossproduct.ui.aspects;

import static java.lang.annotation.ElementType.METHOD;
import static com.vocalink.crossproduct.ui.aspects.EventType.UNKNOWN;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {

  EventType type() default UNKNOWN;

  Positions params();
}
