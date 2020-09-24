package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.SelfFundingSettlementDetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementServiceFacade {
  SettlementDashboardDto getSettlement(String context, ClientType clientType);
  SelfFundingSettlementDetailsDto getSelfFundingSettlementDetails(String context, ClientType clientType, String participantId);

}
