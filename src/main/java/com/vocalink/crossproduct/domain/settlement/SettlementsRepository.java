package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;

public interface SettlementsRepository {

  ParticipantSettlement findSettlement(String context, ParticipantSettlementRequest request, String cycleId, String participantId);
}
