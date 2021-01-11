package com.vocalink.crossproduct.infrastructure.exception;

public class InfrastructureException extends RuntimeException {
  private final String location;

  public InfrastructureException(Throwable cause, String location) {
    super(cause);
    this.location = location;
  }

  public InfrastructureException(String message, Throwable cause, String location) {
    super(message, cause);
    this.location = location;
  }

  public String getLocation() {
    return this.location;
  }
}
