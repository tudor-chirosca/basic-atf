package com.vocalink.crossproduct;

import java.time.Clock;
import java.time.ZonedDateTime;

import static java.time.Clock.fixed;
import static java.time.LocalDateTime.of;
import static java.time.ZoneOffset.UTC;

public interface TestConstants {
  String CONTEXT = "BPS";
  String SCHEME_CODE = "P27";
  String CLIENT_TYPE = "UI";
  String DATE_TIME = "2021-04-21T22:48:56+02:00";
  Clock FIXED_CLOCK = fixed(ZonedDateTime.parse(DATE_TIME).toInstant(), UTC);
}
