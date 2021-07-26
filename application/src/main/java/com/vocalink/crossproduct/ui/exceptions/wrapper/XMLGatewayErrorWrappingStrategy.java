package com.vocalink.crossproduct.ui.exceptions.wrapper;

import com.vocalink.crossproduct.domain.error.GatewayError;
import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.infrastructure.exception.ErrorConstants;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.ui.exceptions.ErrorDescriptionResponse;
import com.vocalink.crossproduct.ui.exceptions.GatewayErrorDescription;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@ConditionalOnProperty(name = "app.error.wrapping", havingValue = "xml", matchIfMissing = true)
public class XMLGatewayErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      InfrastructureException exception) {

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
  public ResponseEntity<ErrorDescriptionResponse> wrapException(ClientRequestException exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();
    error.getErrors().addError(
        GatewayError.builder()
            .reasonCode(ErrorConstants.ERROR_REASON_INVALID_INPUT)
            .description(exception.getMessage())
            .recoverable(false)
            .build());

    return ResponseEntity
        .status(exception.getStatusCode())
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      BindException exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();

    exception.getBindingResult().getAllErrors().forEach(err -> error.getErrors()
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
      MethodArgumentNotValidException exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();

    exception.getBindingResult().getAllErrors().forEach(err -> error.getErrors()
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
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      DecodingException exception) {
    GatewayErrorDescription error = new GatewayErrorDescription();
    error.getErrors().addError(
        GatewayError.builder()
            .source(ErrorConstants.ERROR_SOURCE_BPS)
            .reasonCode(ErrorConstants.ERROR_REASON_INTERNAL_ERROR)
            .description(ErrorConstants.ERROR_REASON_INTEGRATION_ERROR)
            .recoverable(false)
            .build()
    );
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
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
