package com.vocalink.crossproduct.domain.reference;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageReferenceType {

  PAYMENT("Payment"),
  NON_PAYMENT("Non-Payment"),
  INQUIRY("Inquiry"),
  NON_INQUIRY("Non-Inquiry"),
  MESSAGE_STATUS_REPORT("Message-Status-Report"),
  ADMI("ADMI");

  @JsonValue
  final String description;
}
