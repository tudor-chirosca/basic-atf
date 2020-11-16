package com.vocalink.crossproduct.ui.dto;

import lombok.Getter;

public enum DtoProperties {
  SORT("default.sort"),
  ORDER("default.order"),
  OFFSET("default.offset"),
  LIMIT("default.limit");

  @Getter
  private final String property;

  DtoProperties(String property) {
    this.property = property;
  }
}
