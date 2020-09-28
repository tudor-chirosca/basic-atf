package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.CycleRepository;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantRepository;
import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.domain.PositionDetailsRepository;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.SelfFundingSettlementDetailsDto;
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.Presenter;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class SettlementServiceFacadeImpl implements SettlementServiceFacade {

  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;
  private final PresenterFactory presenterFactory;
  private final PositionDetailsRepository positionDetailsRepository;

  @Override
  public SettlementDashboardDto getSettlement(String context, ClientType clientType) {
    List<Participant> participants = participantRepository.findAll(context);
    List<Cycle> cycles = cycleRepository.findAll(context);

    Presenter presenter = presenterFactory.getPresenter(clientType);

    return presenter.presentSettlement(context, cycles, participants);
  }

  @Override
  public SelfFundingSettlementDetailsDto getSelfFundingSettlementDetails(String context,
      ClientType clientType, String participantId) {

    List<PositionDetails> positionsDetails = positionDetailsRepository
        .findByParticipantId(context, participantId);

    List<String> activeCycleIds = positionsDetails.stream().map(PositionDetails::getSessionCode)
        .collect(toList());

    List<Cycle> cycles = cycleRepository.findByIds(context, activeCycleIds);

    if (cycles.size() != positionsDetails.size()) {
      throw new NonConsistentDataException(
          "Number of Cycles is not equal with number of Position Details");
    }

    if (cycles.size() == 0) {
      throw new EntityNotFoundException("No cycles found for participantId: " + participantId);
    }

    Participant participant = participantRepository.findByParticipantId(context, participantId)
        .orElseThrow(() -> new EntityNotFoundException(
            "There is not Participant with id; " + participantId));

    Presenter presenter = presenterFactory.getPresenter(clientType);

    return presenter
        .presentSelfFundingSettlementDetails(context, cycles, positionsDetails, participant);
  }
}
