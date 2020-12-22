package com.vocalink.crossproduct.domain.settlement;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;

public interface SettlementsRepository {

  ParticipantSettlement findSettlement(String context, ParticipantSettlementRequest request, String cycleId, String participantId);

  Page<ParticipantSettlement> findSettlements(String context, SettlementEnquiryRequest request);
}
