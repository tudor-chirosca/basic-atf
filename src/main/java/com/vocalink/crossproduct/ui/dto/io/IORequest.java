package com.vocalink.crossproduct.ui.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IORequest {

  private final String schemeCode;
  private final String participantId;
  private final String date;
}
