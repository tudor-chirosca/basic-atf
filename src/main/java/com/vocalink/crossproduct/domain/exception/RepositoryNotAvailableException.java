package com.vocalink.crossproduct.domain.exception;

public class RepositoryNotAvailableException extends RuntimeException {
  public RepositoryNotAvailableException(String message) {
    super(message);
  }

  public RepositoryNotAvailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
