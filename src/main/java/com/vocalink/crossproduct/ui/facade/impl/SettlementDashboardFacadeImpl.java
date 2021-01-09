package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.CycleRepository;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.PositionRepository;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.facade.SettlementDashboardFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettlementDashboardFacadeImpl implements SettlementDashboardFacade {

  public static final String NOT_AVAILABLE = "NA";

  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;
  private final PresenterFactory presenterFactory;
  private final PositionRepository positionRepository;
  private final IntraDayPositionGrossRepository intraDayPositionGrossRepository;

  @Override
  public SettlementDashboardDto getSettlement(String context, ClientType clientType) {

    List<Cycle> cycles = cycleRepository.findAll();
    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    List<Participant> participants = participantRepository.findAll();

    return presenterFactory.getPresenter(clientType)
        .presentAllParticipantsSettlement(cycles, participants);
  }

  @Override
  public SettlementDashboardDto getSettlement(String context, ClientType clientType,
      String participantId) {

    List<Cycle> cycles = cycleRepository.findAll();

    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    Participant fundingParticipant = participantRepository.findById(participantId);

    List<Participant> participants = participantRepository
        .findWith(participantId, ParticipantType.FUNDED.getDescription());

    List<IntraDayPositionGross> intraDays = intraDayPositionGrossRepository
        .findIntraDayPositionGrossByParticipantIds(participants.stream()
            .map(Participant::getBic).collect(toList()));

    return presenterFactory.getPresenter(clientType)
        .presentFundingParticipantSettlement(cycles, participants, fundingParticipant, intraDays);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto getParticipantSettlementDetails(String context,
      ClientType clientType, String participantId) {

    Participant participant = participantRepository.findById(participantId);

    List<PositionDetails> positionsDetails = positionRepository.findByParticipantId(participantId);

    List<String> activeCycleIds = positionsDetails.stream().map(PositionDetails::getSessionCode)
        .collect(toList());

    List<Cycle> cycles = cycleRepository.findByIds(activeCycleIds);

    if (cycles.isEmpty()) {
      throw new EntityNotFoundException("No cycles found for participantId: " + participantId);
    }

    if (cycles.size() != positionsDetails.size()) {
      throw new NonConsistentDataException(
          "Number of Cycles is not equal with number of Position Details");
    }

    return presenterFactory.getPresenter(clientType)
        .presentParticipantSettlementDetails(cycles, positionsDetails, participant,
            getFundingParticipantFrom(participant),
            getIntraDayPositionGross(participant));
  }

  private Participant getFundingParticipantFrom(Participant participant) {
    Participant fundingParticipant = null;

    if (participant.getFundingBic() != null && !participant.getFundingBic().equals("NA")) {
      fundingParticipant = participantRepository.findById(participant.getFundingBic());
    }
    return fundingParticipant;
  }

  private IntraDayPositionGross getIntraDayPositionGross(Participant participant) {
    IntraDayPositionGross intraDayPositionGross = null;

    if (participant.getFundingBic() != null && !participant.getFundingBic().equals(NOT_AVAILABLE)) {

      intraDayPositionGross = intraDayPositionGrossRepository
          .findIntraDayPositionGrossByParticipantIds(singletonList(participant.getBic()))
          .stream()
          .findFirst()
          .orElseThrow(() -> new EntityNotFoundException(
              "There is no Intra-Day Participant Position gross for participant id: "
                  + participant.getId()));
    }
    return intraDayPositionGross;
  }
}
