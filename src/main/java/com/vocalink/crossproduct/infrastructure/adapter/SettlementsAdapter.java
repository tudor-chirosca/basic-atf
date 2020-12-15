package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementsRepository;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.settlement.CPInstructionEnquiryRequest;
import com.vocalink.crossproduct.shared.settlement.SettlementsClient;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementsAdapter implements SettlementsRepository {

  private final ClientFactory clientFactory;

  @Override
  public ParticipantSettlement findSettlement(String context, ParticipantSettlementRequest request,
      String cycleId, String participantId) {
    final SettlementsClient settlementsClient = clientFactory.getSettlementsClient(context);

    log.info("Fetching settlement for cycleId: {}, participantId: {}, from: {}", cycleId,
        participantId, context);

    CPInstructionEnquiryRequest cpRequest = MAPPER.toCp(request);

    return MAPPER.toEntity(settlementsClient.findBy(cpRequest, cycleId, participantId));
  }
}
