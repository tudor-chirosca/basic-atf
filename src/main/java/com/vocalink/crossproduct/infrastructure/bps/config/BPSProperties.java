package com.vocalink.crossproduct.infrastructure.bps.config;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration("bpsConfiguration")
@ConfigurationProperties("bps-config")
@PropertySource(value = "classpath:bps-config.yaml", factory = YamlPropertySourceFactory.class)
public class BPSProperties {

  private Integer retryCount;
  private Duration timeoutDuration;
  private Map<String, String> baseUrls;
  private Map<String, Detail> paths;
  private List<Integer> retryable;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class Detail {

    private String base;
    private String path;
  }
}
