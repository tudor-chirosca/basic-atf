package com.vocalink.crossproduct.ui.exceptions;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

  public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

  @ExceptionHandler
  public ResponseEntity<ErrorDescription> handleException(
      final HttpServletRequest request,
      final Exception exception) {

    log.error("ERROR on Request: " + request.getRequestURL() + " \ntrace: " +
        ExceptionUtils.getStackTrace(exception));

    ErrorDescription error = ErrorDescription.builder()
        .timestamp(LocalDateTime.now().toString())
        .errorCode(INTERNAL_SERVER_ERROR)
        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .message(exception.getMessage())
        .path(request.getRequestURI())
        .build();

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
