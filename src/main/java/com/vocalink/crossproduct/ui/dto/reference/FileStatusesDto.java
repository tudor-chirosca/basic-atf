package com.vocalink.crossproduct.ui.dto.reference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FileStatusesDto {

  private final String status;
  private final boolean hasReason;
  @JsonInclude(Include.NON_EMPTY)
  private final List<String> reasonCodes;
}
