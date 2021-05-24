package com.vocalink.crossproduct.ui.dto.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Builder
public class MessageDirectionReferenceDto {

  private final String name;
  private final List<String> types;
  @JsonProperty(value="isDefault")
  @Setter
  private boolean isDefault;

}
