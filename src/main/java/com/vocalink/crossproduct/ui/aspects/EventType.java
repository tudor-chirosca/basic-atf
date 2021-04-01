package com.vocalink.crossproduct.ui.aspects;

import lombok.Getter;

@Getter
public enum EventType {

  FILE_SEARCH_ENQUIRY("File Enquiry Search");

  private final String activity;

  EventType(String activity) {
    this.activity = activity;
  }
}
