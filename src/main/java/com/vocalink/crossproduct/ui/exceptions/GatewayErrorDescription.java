package com.vocalink.crossproduct.ui.exceptions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.vocalink.crossproduct.domain.error.Errors;
import lombok.Getter;

@Getter
public class GatewayErrorDescription implements ErrorDescriptionResponse {

  @Getter(onMethod_ = { @JsonGetter("Errors") })
  private final Errors errors = new Errors();
}
