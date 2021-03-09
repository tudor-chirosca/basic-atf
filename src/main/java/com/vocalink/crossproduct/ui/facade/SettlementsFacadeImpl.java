package com.vocalink.crossproduct.ui.facade;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.facade.api.SettlementsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettlementsFacadeImpl implements SettlementsFacade {

  private static final int NR_OF_LATEST_CYCLES = 2;
  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public ParticipantSettlementDetailsDto getSettlementDetails(String product, ClientType clientType,
      ParticipantSettlementRequest request, String cycleId, String participantId) {
    log.info("Fetching settlement for cycleId: {}, participantId: {}, from: {}", cycleId,
        participantId, product);

    final Participant participant = repositoryFactory.getParticipantRepository(product)
        .findById(participantId);

    final ParticipantSettlement participantSettlement = repositoryFactory
        .getSettlementsRepository(product)
        .findBy(MAPPER.toEntity(request, cycleId, participantId));

    final List<Participant> participants = repositoryFactory
        .getParticipantRepository(product)
        .findAll();

    if (participant.getParticipantType().equals(ParticipantType.FUNDED)) {
      Participant fundingParticipant = repositoryFactory.getParticipantRepository(product)
          .findById(participant.getFundingBic());

      return presenterFactory.getPresenter(clientType)
          .presentSettlementDetails(participantSettlement, participants, participant,
              fundingParticipant);
    }

    return presenterFactory.getPresenter(clientType)
        .presentSettlementDetails(participantSettlement, participants, participant);
  }

  @Override
  public PageDto<ParticipantSettlementCycleDto> getSettlements(String product,
      ClientType clientType, SettlementEnquiryRequest request) {
    log.info("Fetching settlements from: {}", product);

    final Page<ParticipantSettlement> participantSettlements = repositoryFactory
        .getSettlementsRepository(product)
        .findPaginated(MAPPER.toEntity(request));

    final ParticipantRepository participantRepository = repositoryFactory
        .getParticipantRepository(product);

    List<Participant> participants = request.getParticipants().stream()
        .map(participantRepository::findById)
        .collect(toList());

    return presenterFactory.getPresenter(clientType)
        .presentSettlements(participantSettlements, participants);
  }

  @Override
  public LatestSettlementCyclesDto getLatestCycles(
      String product, ClientType clientType) {
    List<Cycle> latestCycles = repositoryFactory.getCycleRepository(product)
        .findLatest(NR_OF_LATEST_CYCLES);
    if (latestCycles.size() != NR_OF_LATEST_CYCLES) {
      throw new NonConsistentDataException("Not enough cycles returned based on request parameters");
    }
    return presenterFactory.getPresenter(clientType)
      .presentLatestCycles(latestCycles);
  }

  public SettlementScheduleDto getSettlementsSchedule(String product,
      ClientType clientType) {
    log.info("Fetching settlements schedule from: {}", product);

    final SettlementSchedule schedule = repositoryFactory.getSettlementsRepository(product)
        .findSchedule();

    return presenterFactory.getPresenter(clientType)
        .presentSchedule(schedule);
  }
}
