package com.vocalink.crossproduct.ui.exceptions.wrapper;

import com.vocalink.crossproduct.domain.error.RFCError;
import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.infrastructure.exception.ErrorConstants;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.ui.exceptions.ErrorDescriptionResponse;
import com.vocalink.crossproduct.ui.exceptions.RFCErrorDescription;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
@ConditionalOnProperty(name = "app.error.wrapping", havingValue = "rfc")
public class RFC7807ErrorWrappingStrategy implements ErrorWrappingStrategy {

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
  public ResponseEntity<ErrorDescriptionResponse> wrapException(ClientRequestException exception) {
    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(exception.getStatusCode().value())
        .title(exception.getStatusCode().getReasonPhrase())
        .errorDetails(Collections.singletonList((
            RFCError.builder()
                .reason(ErrorConstants.ERROR_REASON_INVALID_INPUT)
                .message(exception.getMessage())
                .recoverable(false)
                .build()
        )))
        .build();
    return ResponseEntity
        .status(exception.getStatusCode())
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
      MethodArgumentNotValidException exception) {

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
  public ResponseEntity<ErrorDescriptionResponse> wrapException(
      DecodingException exception) {
    RFCErrorDescription error = RFCErrorDescription.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .detail(ErrorConstants.ERROR_REASON_INTEGRATION_ERROR)
        .build();

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
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
