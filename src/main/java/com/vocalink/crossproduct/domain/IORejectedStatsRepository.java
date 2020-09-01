package com.vocalink.crossproduct.domain;

import java.time.LocalDate;

public interface IORejectedStatsRepository {
  IORejectedStats findByTimestamp(LocalDate localDateTime);
}
