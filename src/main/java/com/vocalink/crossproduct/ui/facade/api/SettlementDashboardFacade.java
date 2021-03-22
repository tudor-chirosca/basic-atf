package com.vocalink.crossproduct.ui.facade.api;

import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementDashboardFacade {

  SettlementDashboardDto getParticipantSettlement(String context, ClientType clientType,
      String fundingParticipantId);

  ParticipantDashboardSettlementDetailsDto getParticipantSettlementDetails(String context,
      ClientType clientType, String participantId);
}
