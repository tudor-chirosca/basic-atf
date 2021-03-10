package com.vocalink.crossproduct.infrastructure.bps.configuration;

import com.vocalink.crossproduct.domain.configuration.Configuration;
import com.vocalink.crossproduct.domain.configuration.ConfigurationService;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BPSConfigurationService implements ConfigurationService {

  private final BPSProperties bpsProperties;

  @Override
  public Configuration getConfiguration() {
    return new Configuration(bpsProperties.getSchemeCode(),
        bpsProperties.getCurrencies().get(bpsProperties.getSchemeCode()));
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
