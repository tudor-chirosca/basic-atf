package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementDashboardFacade {

  SettlementDashboardDto getSettlement(String context, ClientType clientType);

  SettlementDashboardDto getParticipantSettlement(String context, ClientType clientType, String participantId);

  ParticipantDashboardSettlementDetailsDto getParticipantSettlementDetails(String context,
      ClientType clientType, String participantId);
}
