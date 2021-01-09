package com.vocalink.crossproduct.ui.exceptions.wrapper;

import com.vocalink.crossproduct.domain.error.RFCError;
import com.vocalink.crossproduct.infrastructure.exception.ErrorConstants;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.shared.exception.AdapterException;
import com.vocalink.crossproduct.ui.exceptions.ErrorDescriptionResponse;
import com.vocalink.crossproduct.ui.exceptions.RFCErrorDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Component
@ConditionalOnProperty(name = "app.error.wrapping", havingValue = "rfc")
public class RFC7807ErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      AdapterException exception) {

    // TODO handle different adapter exception causes when those are propagated
    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .errorDetails(Collections.singletonList((
            RFCError.builder()
                .source(exception.getLocation())
                .reason(ErrorConstants.ERROR_REASON_INTERNAL_ERROR)
                .message(exception.getMessage())
                .recoverable(false)
                .build()
        )))
        .build();

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      InfrastructureException exception) {

    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .errorDetails(Collections.singletonList((
            RFCError.builder()
                .source(exception.getLocation())
                .reason(ErrorConstants.ERROR_REASON_INTERNAL_ERROR)
                .message(exception.getMessage())
                .recoverable(false)
                .build()
        )))
        .build();

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      BindException exception) {

    List<RFCError> errorDetails = new ArrayList<>();
    exception.getBindingResult().getAllErrors().forEach((err) -> errorDetails
        .add(RFCError
            .builder()
            .source(ErrorConstants.ERROR_SOURCE_ISS)
            .reason(ErrorConstants.ERROR_REASON_INVALID_INPUT)
            .message(err.getDefaultMessage())
            .recoverable(false)
            .build()
        ));

    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .detail(ErrorConstants.ERROR_REASON_INVALID_INPUT)
        .errorDetails(errorDetails)
        .build();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      HttpMessageNotReadableException exception) {
    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .detail(exception.getMessage())
        .build();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> wrapException(Exception exception) {
    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .detail(exception.getMessage())
        .build();

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(error);
  }
}
