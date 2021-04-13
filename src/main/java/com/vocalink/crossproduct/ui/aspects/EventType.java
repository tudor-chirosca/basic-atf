package com.vocalink.crossproduct.ui.aspects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

  FILE_SEARCH_ENQUIRY("File Enquiry Search"),
  FILE_DETAILS_ENQUIRY("File Details Search");

  private final String activity;
}
