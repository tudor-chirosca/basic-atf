package com.vocalink.crossproduct.ui.exceptions.wrapper;

import com.vocalink.crossproduct.infrastructure.exception.ClientRequestException;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.ui.exceptions.ErrorDescriptionResponse;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ErrorWrappingStrategy {

  ResponseEntity<ErrorDescriptionResponse> wrapException(BindException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(MethodArgumentNotValidException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(HttpMessageNotReadableException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(DecodingException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(Exception exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(InfrastructureException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(ClientRequestException exception);
}
