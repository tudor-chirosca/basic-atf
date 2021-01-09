package com.vocalink.crossproduct.ui.exceptions.wrapper;

import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.shared.exception.AdapterException;
import com.vocalink.crossproduct.ui.exceptions.ErrorDescriptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;

public interface ErrorWrappingStrategy {

  ResponseEntity<ErrorDescriptionResponse> wrapException(AdapterException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(BindException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(HttpMessageNotReadableException exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(Exception exception);

  ResponseEntity<ErrorDescriptionResponse> wrapException(InfrastructureException exception);
}
