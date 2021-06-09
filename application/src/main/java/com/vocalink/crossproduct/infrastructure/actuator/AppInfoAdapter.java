package com.vocalink.crossproduct.infrastructure.actuator;

import com.vocalink.crossproduct.infrastructure.jpa.info.DatabaseInfo;
import com.vocalink.crossproduct.infrastructure.jpa.info.DatabaseVersionRepositoryJpa;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppInfoAdapter {

  private static final String NOT_AVAILABLE = "n/a";

  private final DatabaseVersionRepositoryJpa databaseVersionRepositoryJpa;

  @Value("${app.version}")
  private String pomVersion;

  public Map<String, String> getLatestMigrationVersion() {
    final Optional<DatabaseInfo> latestInfo = databaseVersionRepositoryJpa
        .findFirstByOrderByReleaseDateDesc();

    final Map<String, String> map = new HashMap<>();
    map.put("version", latestInfo.map(DatabaseInfo::getVersion).orElse(NOT_AVAILABLE));
    map.put("releaseDate", latestInfo.map(DatabaseInfo::getReleaseDate).orElse(NOT_AVAILABLE));

    return map;
  }

  public String getPomVersion() {
    return pomVersion == null ? NOT_AVAILABLE : pomVersion;
  }
}
