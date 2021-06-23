package com.vocalink.crossproduct.domain.reference;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageReferenceDirection {

  SENDING("Sending"),
  RECEIVING("Receiving");

  @JsonValue
  final String description;
}
