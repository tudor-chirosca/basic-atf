package com.vocalink.crossproduct.domain.reference;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageDirectionReference {

  private final String messageType;
  private final String formatName;
  private final List<MessageReferenceDirection> direction;
  private final List<MessageReferenceLevel> level;
  private final List<MessageReferenceType> subType;
}
