package com.vocalink.crossproduct.infrastructure.bps.config;

import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties.Detail;
import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BPSPathUtils {

  public static URI resolve(String path, BPSProperties properties) {

    final Detail detail = properties.getPaths().get(path);

    final String baseUrl = properties.getBaseUrls().get(detail.getBase());

    return UriComponentsBuilder.fromUriString(baseUrl)
        .path(detail.getPath())
        .build(properties.getSchemeCode());
  }
}
