package com.vocalink.crossproduct.infrastructure.bps.config;

import static org.apache.commons.lang3.EnumUtils.getEnumIgnoreCase;

import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
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
  private String schemeCode;
  private Integer ioDetailsThreshold;
  private Map<String, String> currencies;
  private Map<String, String> baseUrls;
  private Map<String, Detail> paths;
  private List<Integer> retryable;
  private Map<String, String> eodPeriod;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class Detail {

    private String base;
    private String path;
  }

  @PostConstruct
  protected void validateLoadedSchemeCode() {
    final String stringEnum = schemeCode.replace("-", "_");

    if (getEnumIgnoreCase(BPSSchema.class, stringEnum) == null) {
      throw new InfrastructureException("Invalid scheme name provided: " + stringEnum);
    }
  }
}
