package com.vocalink.crossproduct.infrastructure.bps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BPSSortingQuery {

  private final String sortOrderBy;
  private final BPSSortOrder sortOrder;
}
