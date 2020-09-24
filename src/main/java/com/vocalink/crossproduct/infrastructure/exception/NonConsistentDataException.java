package com.vocalink.crossproduct.infrastructure.exception;

public class NonConsistentDataException extends RuntimeException {

  public NonConsistentDataException(String message) {
    super(message);
  }
}
