package com.vocalink.crossproduct.infrastructure.bps.config;

import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties.Detail;
import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BPSPathUtils {

  public static URI resolve(String path, BPSProperties properties) {
    return resolve(path, properties, properties.getSchemeCode());
  }

  public static URI resolve(String path, BPSProperties properties, String... params) {

    final Detail detail = properties.getPaths().get(path);

    final String baseUrl = properties.getBaseUrls().get(detail.getBase());

    return UriComponentsBuilder.fromUriString(baseUrl)
        .path(detail.getPath())
        .build((Object[]) params);
  }
}
