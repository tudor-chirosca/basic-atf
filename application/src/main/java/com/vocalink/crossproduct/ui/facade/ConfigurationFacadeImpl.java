package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT;
import static com.vocalink.crossproduct.ui.dto.DtoProperties.TIME_ZONE;
import static java.lang.Integer.parseInt;

import com.vocalink.crossproduct.ServiceFactory;
import com.vocalink.crossproduct.domain.configuration.Configuration;
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.facade.api.ConfigurationFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigurationFacadeImpl implements ConfigurationFacade {

  private final PresenterFactory presenterFactory;
  private final ServiceFactory serviceFactory;

  @Override
  public ConfigurationDto getConfiguration(String product, ClientType clientType) {
    log.info("Fetching configuration for : {}", product);

    final Configuration configuration = serviceFactory.getConfigurationService(product)
        .getConfiguration();

    final Integer dataRetentionDays = parseInt(getDefault(DAYS_LIMIT));
    final String timeZone = getDefault(TIME_ZONE);

    return presenterFactory.getPresenter(clientType)
        .presentConfiguration(configuration, dataRetentionDays, timeZone);
  }
}
