package com.vocalink.crossproduct.ui.facade.impl;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
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

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public SettlementDashboardDto getSettlement(String product, ClientType clientType) {

    List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findAll();

    if (cycles.size() < 2) {
      throw new NonConsistentDataException("Expected at least two cycles!");
    }

    List<Participant> participants = repositoryFactory.getParticipantRepository(product).findAll()
        .stream().filter(p -> p.getParticipantType() != ParticipantType.SCHEME_OPERATOR).collect(toList());

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
        .findById(participantId);

    return presenterFactory.getPresenter(clientType)
        .presentFundingParticipantSettlement(cycles, participants, fundingParticipant, intraDays);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto getParticipantSettlementDetails(String product,
      ClientType clientType, String participantId) {

    Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    List<ParticipantPosition> positions = repositoryFactory.getPositionRepository(product)
        .findByParticipantId(participantId);

    List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findLatest(2);

    if (cycles.isEmpty()) {
      throw new EntityNotFoundException("No cycles found for participantId: " + participantId);
    }

    if (cycles.size() != positions.size()) {
      throw new NonConsistentDataException(
          "Number of Cycles is not equal with number of Position Details");
    }

    if (participant.getParticipantType() == ParticipantType.FUNDED) {
      Participant fundingParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(participant.getFundingBic());

      IntraDayPositionGross intraDayPositionGross = repositoryFactory
          .getIntradayPositionGrossRepository(product)
          .findById(participant.getFundingBic())
          .stream()
          .filter(f -> f.getDebitParticipantId().equals(participantId))
          .findFirst()
          .orElseThrow(() -> new EntityNotFoundException(
              "There is no Intra-Day Participant Position gross for participant id: "
                  + participant.getId()));

      return presenterFactory.getPresenter(clientType)
          .presentFundedParticipantSettlementDetails(cycles, positions, participant,
              fundingParticipant,
              intraDayPositionGross);
    }

    return presenterFactory.getPresenter(clientType)
        .presentParticipantSettlementDetails(cycles, positions, participant);
  }
}
