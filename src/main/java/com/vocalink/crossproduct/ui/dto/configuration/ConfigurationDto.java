package com.vocalink.crossproduct.ui.dto.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConfigurationDto {

  private final String scheme;
  private final String schemeCurrency;
  private final Integer dataRetentionDays;
  private final Integer ioDetailsThreshold;
}
