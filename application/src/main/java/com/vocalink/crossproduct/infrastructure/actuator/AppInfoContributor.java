package com.vocalink.crossproduct.infrastructure.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppInfoContributor implements InfoContributor {

  private static final String ISS_VERSION = "issVersion";
  private static final String MIGRATION = "migration";

  private final AppInfoAdapter infoAdapter;

  @Override
  public void contribute(Builder builder) {
    builder.withDetail(ISS_VERSION, infoAdapter.getPomVersion());
    builder.withDetail(MIGRATION, infoAdapter.getLatestMigrationVersion());
  }
}
