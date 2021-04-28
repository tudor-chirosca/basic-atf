package com.vocalink.crossproduct.domain.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Configuration {

  private final String scheme;
  private final String schemeCurrency;
  private final Integer ioDetailsThreshold;
}
