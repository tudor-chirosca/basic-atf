package com.vocalink.crossproduct.ui.exceptions;

import java.time.Clock;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  private final Clock clock = Clock.systemUTC();

  @ExceptionHandler
  public ResponseEntity<ErrorDescription> handleException(
      final HttpServletRequest request,
      final Exception exception) {

    log.error("ERROR on Request: {} {}", request.getRequestURL(), exception.getMessage());

    ErrorDescription error = ErrorDescription.builder()
        .timestamp(LocalDateTime.now(clock).toString())
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message(exception.getMessage())
        .additionalInfo("")
        .path(request.getRequestURI())
        .build();

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
