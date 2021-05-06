package com.vocalink.crossproduct;

import java.time.Clock;

import static java.time.Clock.fixed;
import static java.time.LocalDateTime.of;
import static java.time.ZoneOffset.UTC;

public interface TestConstants {
  String CONTEXT = "BPS";
  String SCHEME_CODE = "P27";
  String CLIENT_TYPE = "UI";
  Clock FIXED_CLOCK = fixed(of(2021, 4, 21, 22, 48, 56).toInstant(UTC), UTC);
}
