package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.SettlementDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementServiceFacade {
  SettlementDto getSettlement(String context, ClientType clientType);
}
