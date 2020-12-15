package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.PositionDetailsRepository;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettlementServiceFacadeImpl implements SettlementServiceFacade {

  public static final String NOT_AVAILABLE = "NA";
  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;
  private final PresenterFactory presenterFactory;
  private final PositionDetailsRepository positionDetailsRepository;
  private final IntraDayPositionGrossRepository intraDayPositionGrossRepository;

  @Override
  public SettlementDashboardDto getSettlement(String context, ClientType clientType) {

    List<Cycle> cycles = cycleRepository.findAll(context);
    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    List<Participant> participants = participantRepository.findAll(context);

    return presenterFactory.getPresenter(clientType)
        .presentAllParticipantsSettlement(cycles, participants);
  }

  @Override
  public SettlementDashboardDto getSettlement(String context, ClientType clientType,
      String participantId) {

    List<Cycle> cycles = cycleRepository.findAll(context);

    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    Participant fundingParticipant = participantRepository.findByParticipantId(context, participantId)
        .orElseThrow(() -> new EntityNotFoundException(
            "There is no Participant with id: " + participantId));

    List<Participant> participants = participantRepository.findAll(context).stream()
        .filter(p -> p.getFundingBic().equals(participantId))
        .collect(toList());

    if (participants.isEmpty()) {
      throw new EntityNotFoundException(
          "There are no funded participants for  id: " + participantId);
    }

    List<IntraDayPositionGross> intraDays = intraDayPositionGrossRepository
        .findIntraDayPositionGrossByParticipantId(context, participants.stream()
            .map(Participant::getBic).collect(toList()));

    return presenterFactory.getPresenter(clientType)
        .presentFundingParticipantSettlement(cycles, participants, fundingParticipant, intraDays);
  }

  @Override
  public ParticipantSettlementDetailsDto getParticipantSettlementDetails(String context,
      ClientType clientType, String participantId) {

    Participant participant = participantRepository.findByParticipantId(context, participantId)
        .orElseThrow(() -> new EntityNotFoundException(
            "There is no Participant with id: " + participantId));

    List<PositionDetails> positionsDetails = positionDetailsRepository
        .findByParticipantId(context, participantId);

    List<String> activeCycleIds = positionsDetails.stream().map(PositionDetails::getSessionCode)
        .collect(toList());

    List<Cycle> cycles = cycleRepository.findByIds(context, activeCycleIds);

    if (cycles.isEmpty()) {
      throw new EntityNotFoundException("No cycles found for participantId: " + participantId);
    }

    if (cycles.size() != positionsDetails.size()) {
      throw new NonConsistentDataException(
          "Number of Cycles is not equal with number of Position Details");
    }

    return presenterFactory.getPresenter(clientType)
        .presentParticipantSettlementDetails(cycles, positionsDetails, participant,
            getFundingParticipant(context, participant),
            getIntraDayPositionGross(context, participant));
  }

  private Participant getFundingParticipant(String context, Participant participant) {
    Participant fundingParticipant = null;

    if (participant.getFundingBic() != null && !participant.getFundingBic().equals("NA")) {
      fundingParticipant = participantRepository
          .findByParticipantId(context, participant.getFundingBic())
          .orElseThrow(() -> new EntityNotFoundException(
              "There is no Funding Participant with id: " + participant.getFundingBic()));

    }
    return fundingParticipant;
  }

  private IntraDayPositionGross getIntraDayPositionGross(String context, Participant participant) {
    IntraDayPositionGross intraDayPositionGross = null;

    if (participant.getFundingBic() != null && !participant.getFundingBic().equals(NOT_AVAILABLE)) {

      intraDayPositionGross = intraDayPositionGrossRepository
          .findIntraDayPositionGrossByParticipantId(context,
              Collections.singletonList(participant.getBic()))
          .stream()
          .findFirst()
          .orElseThrow(() -> new EntityNotFoundException(
              "There is no Intra-Day Participant Position gross for participant id: "
                  + participant.getId()));
    }
    return intraDayPositionGross;
  }
}
