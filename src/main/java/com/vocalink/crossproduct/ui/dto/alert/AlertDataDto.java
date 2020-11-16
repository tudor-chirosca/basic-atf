package com.vocalink.crossproduct.ui.dto.alert;

import static java.util.Collections.emptyList;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Getter;

@Getter
public class AlertDataDto {

  private final int totalResults;
  private final List<AlertDto> items;

  @JsonCreator
  public AlertDataDto(int totalResults,
      List<AlertDto> items) {
    this.totalResults = totalResults;
    this.items = items == null ? emptyList() : items;
  }
}
