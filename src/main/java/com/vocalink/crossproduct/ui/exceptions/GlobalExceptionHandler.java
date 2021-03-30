package com.vocalink.crossproduct.ui.exceptions;

import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.ui.exceptions.wrapper.ErrorWrappingStrategy;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ErrorWrappingStrategy errorWrapper;

  @ExceptionHandler
  public ResponseEntity<ErrorDescriptionResponse> handleException(
      final HttpServletRequest request,
      final Exception exception) {

    log.error("ERROR on Request: {} {}", request.getRequestURL(), exception.getMessage());
    return errorWrapper.wrapException(exception);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleHttpsConversionException(
      final HttpServletRequest request,
      final HttpMessageNotReadableException exception) {

    log.error("ERROR on Request: {} {}", request.getRequestURL(), exception.getMessage());

    return errorWrapper.wrapException(exception);
  }

  @ExceptionHandler({BindException.class})
  public ResponseEntity<ErrorDescriptionResponse> handleBindException(
      final HttpServletRequest request,
      final BindException exception) {

    log.error("ERROR on Request: {} {}", request.getRequestURL(), exception.getMessage());

    return errorWrapper.wrapException(exception);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorDescriptionResponse> handleBindException(
      final HttpServletRequest request,
      final MethodArgumentNotValidException exception) {

    log.error("ERROR on Request: {} {}", request.getRequestURL(), exception.getMessage());

    return errorWrapper.wrapException(exception);
  }

  @ExceptionHandler({ClientRequestException.class})
  public ResponseEntity<ErrorDescriptionResponse> handleClientRequestException(
      final HttpServletRequest request,
      final ClientRequestException exception) {

    log.error("ERROR on Request: {} {}", request.getRequestURL(), exception.getMessage());

    return errorWrapper.wrapException(exception);
  }
}
