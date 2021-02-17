package com.vocalink.crossproduct.infrastructure.bps;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSSortingQuery {

  private final String sortOrderBy;
  private final BPSSortOrder sortOrder;
}
