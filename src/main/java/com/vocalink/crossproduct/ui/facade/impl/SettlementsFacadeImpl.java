package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.Collections.singletonList;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementsRepository;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.facade.SettlementsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementsFacadeImpl implements SettlementsFacade {

  private final PresenterFactory presenterFactory;
  private final SettlementsRepository settlementsRepository;
  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;

  @Override
  public ParticipantSettlementDetailsDto getDetailsBy(String context, ClientType clientType,
      ParticipantSettlementRequest request, String cycleId, String participantId) {

    final ParticipantSettlement participantSettlement = settlementsRepository
        .findSettlement(context, request, cycleId, participantId);

    final List<Participant> participants = participantRepository.findAll(context);

    Cycle cycle = cycleRepository.findByIds(context, singletonList(cycleId)).stream()
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException("There is no Cycle with id: " + cycleId));


    return presenterFactory.getPresenter(clientType)
        .presentSettlementDetails(participantSettlement, participants, cycle);
  }
}
