package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface ConfigurationFacade {

  ConfigurationDto getConfiguration(String product, ClientType clientType);
}
