package com.vocalink.crossproduct.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class MessageDirectionReference {

  private final String messageType;
  private final String messageDirection;
}
