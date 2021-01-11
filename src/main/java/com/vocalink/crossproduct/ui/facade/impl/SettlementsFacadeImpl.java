package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementsRepository;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
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

  @Override
  public ParticipantSettlementDetailsDto getDetailsBy(String context, ClientType clientType,
      ParticipantSettlementRequest request, String cycleId, String participantId) {

    final ParticipantSettlement participantSettlement = settlementsRepository
        .findSettlement(context, request, cycleId, participantId);

    final List<Participant> participants = participantRepository.findAll();

    return presenterFactory.getPresenter(clientType)
        .presentSettlementDetails(participantSettlement, participants);
  }

  @Override
  public PageDto<ParticipantSettlementCycleDto> getSettlements(String context,
      ClientType clientType, SettlementEnquiryRequest request) {

    final Page<ParticipantSettlement> participantSettlements = settlementsRepository
        .findSettlements(context, request);

    List<Participant> participants = request.getParticipants().stream()
        .map(participantRepository::findById)
        .collect(toList());

    return presenterFactory.getPresenter(clientType)
        .presentSettlements(participantSettlements, participants);
  }
}
