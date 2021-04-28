package com.vocalink.crossproduct.domain.exception;

public class ServiceNotAvailableException extends RuntimeException {

  public ServiceNotAvailableException(String message) {
    super(message);
  }

  public ServiceNotAvailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
