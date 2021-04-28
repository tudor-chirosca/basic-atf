package com.vocalink.crossproduct.ui.dto.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IOTransactionsMessageTypesDto {

  private final String name;
  private final String code;
  private final IODataAmountDetailsDto data;
}
