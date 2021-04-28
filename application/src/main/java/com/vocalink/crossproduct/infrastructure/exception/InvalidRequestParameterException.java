package com.vocalink.crossproduct.infrastructure.exception;

public class InvalidRequestParameterException extends RuntimeException {

  public InvalidRequestParameterException(String message) {
    super(message);
  }

}
