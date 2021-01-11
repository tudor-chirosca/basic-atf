package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
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

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public SettlementDashboardDto getSettlement(String product, ClientType clientType) {

    List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findAll();

    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    List<Participant> participants = repositoryFactory.getParticipantRepository(product).findAll();

    return presenterFactory.getPresenter(clientType)
        .presentAllParticipantsSettlement(cycles, participants);
  }

  @Override
  public SettlementDashboardDto getParticipantSettlement(String product, ClientType clientType,
      String participantId) {

    List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findAll();

    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    Participant fundingParticipant = repositoryFactory.getParticipantRepository(product).findById(participantId);

    List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findByConnectingPartyAndType(participantId, ParticipantType.FUNDED.getDescription());

    List<IntraDayPositionGross> intraDays = repositoryFactory
        .getIntradayPositionGrossRepository(product)
        .findByIds(participants.stream()
            .map(Participant::getBic).collect(toList()));

    return presenterFactory.getPresenter(clientType)
        .presentFundingParticipantSettlement(cycles, participants, fundingParticipant, intraDays);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto getParticipantSettlementDetails(String product,
      ClientType clientType, String participantId) {

    Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    List<PositionDetails> positionsDetails = repositoryFactory.getPositionRepository(product)
        .findByParticipantId(participantId);

    List<String> activeCycleIds = positionsDetails.stream().map(PositionDetails::getSessionCode)
        .collect(toList());

    List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findByIds(activeCycleIds);

    if (cycles.isEmpty()) {
      throw new EntityNotFoundException("No cycles found for participantId: " + participantId);
    }

    if (cycles.size() != positionsDetails.size()) {
      throw new NonConsistentDataException(
          "Number of Cycles is not equal with number of Position Details");
    }

    return presenterFactory.getPresenter(clientType)
        .presentParticipantSettlementDetails(cycles, positionsDetails, participant,
            getFundingParticipantFrom(product, participant),
            getIntraDayPositionGross(product, participant));
  }

  private Participant getFundingParticipantFrom(String product, Participant participant) {
    Participant fundingParticipant = null;

    if (participant.getFundingBic() != null && !participant.getFundingBic().equals("NA")) {
      fundingParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(participant.getFundingBic());
    }
    return fundingParticipant;
  }

  private IntraDayPositionGross getIntraDayPositionGross(String product, Participant participant) {
    IntraDayPositionGross intraDayPositionGross = null;

    if (participant.getFundingBic() != null && !participant.getFundingBic().equals(NOT_AVAILABLE)) {
      intraDayPositionGross = repositoryFactory.getIntradayPositionGrossRepository(product)
          .findByIds(singletonList(participant.getBic()))
          .stream()
          .findFirst()
          .orElseThrow(() -> new EntityNotFoundException(
              "There is no Intra-Day Participant Position gross for participant id: "
                  + participant.getId()));
    }
    return intraDayPositionGross;
  }
}
