package com.vocalink.crossproduct.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

  public static final String ENTITY_NOT_FOUND = "Entity not found";

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException() {
    super(ENTITY_NOT_FOUND);
  }
}
