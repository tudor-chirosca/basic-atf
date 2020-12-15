package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface SettlementsFacade {

  ParticipantSettlementDetailsDto getDetailsBy(String context, ClientType clientType,
      ParticipantSettlementRequest request, String cycleId, String participantId);

}
