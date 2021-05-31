package com.vocalink.crossproduct.ui.facade;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementDashboardRequest;
import com.vocalink.crossproduct.ui.facade.api.SettlementDashboardFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SettlementDashboardFacadeImpl implements SettlementDashboardFacade {

  private final RepositoryFactory repositoryFactory;
  private final PresenterFactory presenterFactory;

  @Override
  public SettlementDashboardDto getParticipantSettlement(String product, ClientType clientType,
      SettlementDashboardRequest settlementDashboardRequest) {

    final List<Cycle> cycles = repositoryFactory.getCycleRepository(product).findAll();
    
    final String fundingParticipantId = settlementDashboardRequest.getFundingParticipantId();

    if (nonNull(fundingParticipantId)) {
      final Participant fundingParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(fundingParticipantId);

      final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
          .findByConnectingPartyAndType(fundingParticipantId,
              ParticipantType.FUNDED.getDescription()).getItems();

      final List<IntraDayPositionGross> intraDays = repositoryFactory
          .getIntradayPositionGrossRepository(product)
          .findById(fundingParticipantId);

      return presenterFactory.getPresenter(clientType)
          .presentFundingParticipantSettlement(cycles, participants, fundingParticipant, intraDays);
    }

    Predicate<Participant> scheme = p -> p.getParticipantType() != ParticipantType.SCHEME_OPERATOR;
    Predicate<Participant> tpsp = p -> p.getParticipantType() != ParticipantType.TPSP;
    Predicate<Participant> funded = p -> p.getParticipantType() != ParticipantType.FUNDED;

    final List<Participant> participants = repositoryFactory.getParticipantRepository(product)
        .findAll()
        .getItems()
        .stream()
        .filter(Stream.of(scheme, tpsp, funded).reduce(x -> true, Predicate::and))
        .collect(toList());

    return presenterFactory.getPresenter(clientType)
        .presentAllParticipantsSettlement(cycles, participants);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto getParticipantSettlementDetails(String product,
      ClientType clientType, String participantId) {

    final Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    final List<ParticipantPosition> positions = repositoryFactory.getPositionRepository(product)
        .findByParticipantId(participantId);

    final List<Cycle> cycles = repositoryFactory.getCycleRepository(product)
        .findLatest(2);

    if (participant.getParticipantType().equals(ParticipantType.FUNDED)) {
      final Participant fundingParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(participant.getFundingBic());

      final IntraDayPositionGross intraDayPositionGross = repositoryFactory
          .getIntradayPositionGrossRepository(product)
          .findById(participant.getFundingBic())
          .stream()
          .filter(f -> f.getDebitParticipantId().equals(participantId))
          .findFirst()
          .orElse(IntraDayPositionGross.builder().build());

      return presenterFactory.getPresenter(clientType)
          .presentFundedParticipantSettlementDetails(cycles, positions, participant,
              fundingParticipant,
              intraDayPositionGross);
    }

    return presenterFactory.getPresenter(clientType)
        .presentParticipantSettlementDetails(cycles, positions, participant);
  }
}
