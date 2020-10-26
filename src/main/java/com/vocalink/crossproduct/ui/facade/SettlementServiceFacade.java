package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementServiceFacade {

  SettlementDashboardDto getSettlement(String context, ClientType clientType);

  SettlementDashboardDto getSettlement(String context, ClientType clientType, String participantId);

  ParticipantSettlementDetailsDto getParticipantSettlementDetails(String context,
      ClientType clientType, String participantId);

}
