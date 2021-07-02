package com.vocalink.crossproduct.ui.dto.reference;

import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection;
import com.vocalink.crossproduct.domain.reference.MessageReferenceLevel;
import com.vocalink.crossproduct.domain.reference.MessageReferenceType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageDirectionReferenceDto {

  private final String messageType;
  private final String formatName;
  private final List<MessageReferenceDirection> direction;
  private final List<MessageReferenceLevel> level;
  private final List<MessageReferenceType> subType;

}
