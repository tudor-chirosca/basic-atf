package com.vocalink.crossproduct.ui.dto.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDirectionReferenceDto {

  private String name;
  private List<String> types;
  @JsonProperty(value="isDefault")
  private boolean isDefault;

}
