package com.vocalink.crossproduct.ui.dto.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IOBatchesMessageTypesDto {

  private final String name;
  private final String code;
  private final IODataDetailsDto data;
}
