package com.vocalink.crossproduct.domain.files;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FileReference {

  private final String status;
  private final boolean hasReason;
  private final List<String> reasonCodes;
}
