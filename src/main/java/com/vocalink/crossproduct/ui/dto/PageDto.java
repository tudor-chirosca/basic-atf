package com.vocalink.crossproduct.ui.dto;

import static java.util.Collections.emptyList;

import java.util.List;
import lombok.Getter;

@Getter
public class PageDto {

  private final int totalResults;
  private final List<?> items;

  public PageDto(final int totalResults, final List<?> items) {
    this.totalResults = totalResults;
    this.items = items == null ? emptyList() : items;
  }
}
