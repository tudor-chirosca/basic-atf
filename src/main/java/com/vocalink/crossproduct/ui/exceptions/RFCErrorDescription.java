package com.vocalink.crossproduct.ui.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.error.RFCError;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class RFCErrorDescription implements ErrorDescriptionResponse {

  private final int status;
  private final String detail;
  private final String title;
  private final String type;
  private final String correlationId;
  private final List<RFCError> errorDetails;
}
