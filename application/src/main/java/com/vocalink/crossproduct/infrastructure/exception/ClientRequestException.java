package com.vocalink.crossproduct.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ClientRequestException extends HttpClientErrorException {

  private final String message;

  public ClientRequestException(HttpStatus statusType, String message) {
    super(statusType);
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
