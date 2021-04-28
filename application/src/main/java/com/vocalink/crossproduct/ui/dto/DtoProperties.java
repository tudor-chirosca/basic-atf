package com.vocalink.crossproduct.ui.dto;

import lombok.Getter;

public enum DtoProperties {
  SORT("app.ui.config.default.sort"),
  OFFSET("app.ui.config.default.offset"),
  LIMIT("app.ui.config.default.limit"),
  DAYS_LIMIT("app.ui.config.default.daysLimit"),
  TIME_ZONE("app.ui.config.default.timeZone");

  @Getter
  private final String property;

  DtoProperties(String property) {
    this.property = property;
  }
}
