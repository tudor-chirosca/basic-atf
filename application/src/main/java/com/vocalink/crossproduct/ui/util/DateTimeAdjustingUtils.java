package com.vocalink.crossproduct.ui.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateTimeAdjustingUtils {

  static public ZonedDateTime adjustWithZoneId(ZonedDateTime dateTime, String zoneId) {
    if (dateTime == null) {
      return null;
    }
    final LocalDateTime local = LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.of(zoneId));
    return ZonedDateTime.of(local, ZoneId.of("UTC")).withZoneSameInstant(ZoneOffset.UTC);
  }
}
