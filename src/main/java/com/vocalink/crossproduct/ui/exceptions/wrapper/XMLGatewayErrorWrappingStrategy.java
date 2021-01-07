package com.vocalink.crossproduct.ui.exceptions.wrapper;

import com.vocalink.crossproduct.domain.error.GatewayError;
import com.vocalink.crossproduct.infrastructure.exception.ErrorConstants;
import com.vocalink.crossproduct.shared.exception.AdapterException;
import com.vocalink.crossproduct.ui.exceptions.ErrorDescriptionResponse;
import com.vocalink.crossproduct.ui.exceptions.GatewayErrorDescription;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
@ConditionalOnProperty(name = "app.error.wrapping", havingValue = "xml", matchIfMissing = true)
public class XMLGatewayErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      AdapterException exception) {

    // TODO handle different adapter exception causes when those are propagated
    GatewayErrorDescription error = new GatewayErrorDescription();
    error.getErrors().addError(
        GatewayError.builder()
            .source(exception.getLocation())
            .reasonCode(ErrorConstants.ERROR_REASON_INTERNAL_ERROR)
            .description(exception.getMessage())
            .recoverable(false)
            .build()
    );
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      BindException exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();

    exception.getBindingResult().getAllErrors().forEach((err) -> error.getErrors()
        .addError(GatewayError
            .builder()
            .source(ErrorConstants.ERROR_SOURCE_ISS)
            .reasonCode(ErrorConstants.ERROR_REASON_INVALID_INPUT)
            .description(err.getDefaultMessage())
            .recoverable(false)
            .build()
        ));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      HttpMessageNotReadableException exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();
    error.getErrors().addError(
        GatewayError.builder()
            .source(ErrorConstants.ERROR_SOURCE_ISS)
            .reasonCode(ErrorConstants.ERROR_REASON_INVALID_INPUT)
            .description(exception.getMessage())
            .recoverable(false)
            .build()
    );
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(Exception exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();
    error.getErrors().addError(
        GatewayError.builder()
            .source(ErrorConstants.ERROR_SOURCE_ISS)
            .reasonCode(ErrorConstants.ERROR_REASON_INTERNAL_ERROR)
            .description(exception.getMessage())
            .recoverable(false)
            .build()
    );
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }
}
