package com.vocalink.crossproduct.ui.presenter;

import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODataDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UIPresenter implements Presenter {

  private final SelfFundingSettlementDetailsMapper selfFundingDetailsMapper;

  @Override
  public SettlementDashboardDto presentAllParticipantsSettlement(List<Cycle> cycles,
      List<Participant> participants) {

    cycles = cycles.stream()
        .sorted(Comparator.comparing(Cycle::getId))
        .limit(2)
        .collect(toList());

    Cycle currentCycle = cycles.get(1);
    Cycle previousCycle = cycles.get(0);

    Map<String, ParticipantPosition> positionsCurrentCycle = currentCycle.getTotalPositions()
        .stream()
        .collect(Collectors.toMap(ParticipantPosition::getParticipantId, Function.identity()));

    Map<String, ParticipantPosition> positionsPreviousCycle = previousCycle.getTotalPositions()
        .stream()
        .collect(Collectors.toMap(ParticipantPosition::getParticipantId, Function.identity()));

    List<TotalPositionDto> settlementPositionDtos = new ArrayList<>();
    for (Participant participant : participants) {
      settlementPositionDtos.add(
          TotalPositionDto.builder()
              .currentPosition(MAPPER.toDto(positionsCurrentCycle.get(participant.getId())))
              .previousPosition(MAPPER.toDto(positionsPreviousCycle.get(participant.getId())))
              .participant(participant)
              .build()
      );
    }

    return SettlementDashboardDto.builder()
        .positions(settlementPositionDtos)
        .currentCycle(MAPPER.toDto(currentCycle))
        .previousCycle(MAPPER.toDto(previousCycle))
        .build();
  }

  @Override
  public SettlementDashboardDto presentFundingParticipantSettlement(List<Cycle> cycles,
      List<Participant> participants, Participant fundingParticipant,
      List<IntraDayPositionGross> intraDays) {

    cycles = cycles.stream()
        .sorted(Comparator.comparing(Cycle::getId))
        .limit(2)
        .collect(toList());

    Cycle currentCycle = cycles.get(1);
    Cycle previousCycle = cycles.get(0);

    Map<String, ParticipantPosition> positionsCurrentCycle = currentCycle.getTotalPositions()
        .stream()
        .collect(Collectors.toMap(ParticipantPosition::getParticipantId, Function.identity()));

    Map<String, ParticipantPosition> positionsPreviousCycle = previousCycle.getTotalPositions()
        .stream()
        .collect(Collectors.toMap(ParticipantPosition::getParticipantId, Function.identity()));

    List<TotalPositionDto> positionsDto = new ArrayList<>();
    for (Participant participant : participants) {
      positionsDto.add(
          TotalPositionDto.builder()
              .participant(participant)
              .intraDayPositionGross(intraDays.stream()
                  .filter(pos -> pos.getParticipantId().equals(participant.getBic())).findFirst()
                  .map(MAPPER::toDto)
                  .orElse(IntraDayPositionGrossDto.builder().build()))
              .currentPosition(MAPPER.toDto(positionsCurrentCycle.get(participant.getId())))
              .previousPosition(MAPPER.toDto(positionsPreviousCycle.get(participant.getId())))
              .participant(participant)
              .build()
      );
    }

    PositionDetailsTotalsDto currentPositionTotals = countPositionTotals(positionsDto.stream()
        .map(TotalPositionDto::getCurrentPosition).collect(toList()));

    PositionDetailsTotalsDto previousPositionTotals = countPositionTotals(positionsDto.stream()
        .map(TotalPositionDto::getPreviousPosition).collect(toList()));

    return SettlementDashboardDto.builder()
        .fundingParticipant(MAPPER.toDto(fundingParticipant))
        .currentPositionTotals(currentPositionTotals)
        .previousPositionTotals(previousPositionTotals)
        .intraDayPositionTotalsDto(countIntraDayTotals(intraDays))
        .currentCycle(MAPPER.toDto(currentCycle))
        .previousCycle(MAPPER.toDto(previousCycle))
        .positions(positionsDto)
        .build();
  }

  @Override
  public ParticipantSettlementDetailsDto presentParticipantSettlementDetails(List<Cycle> cycles,
      List<PositionDetails> positionsDetails, Participant participant,
      Participant fundingParticipant, IntraDayPositionGross intradayPositionGross) {

    if (cycles.size() == 1) {
      return selfFundingDetailsMapper
          .presentOneCycleParticipantSettlementDetails(cycles, positionsDetails, participant,
              fundingParticipant, intradayPositionGross);
    }
    return selfFundingDetailsMapper
        .presentFullParticipantSettlementDetails(cycles, positionsDetails, participant,
            fundingParticipant, intradayPositionGross);
  }

  @Override
  public IODashboardDto presentInputOutput(List<Participant> participants,
      List<ParticipantIOData> ioData, LocalDate date) {

    Map<String, Participant> participantsById = participants.stream().collect(
        Collectors.toMap(Participant::getId, Function.identity()));

    double totalFilesRejected = 0.0;
    double totalBatchesRejected = 0.0;
    double totalTransactionsRejected = 0.0;

    List<ParticipantIODataDto> participantIODataDtos = new ArrayList<>();
    for (ParticipantIOData participantIOData : ioData) {
      totalBatchesRejected += participantIOData.getBatches().getRejected();
      totalFilesRejected += participantIOData.getFiles().getRejected();
      totalTransactionsRejected += participantIOData.getTransactions().getRejected();

      participantIODataDtos.add(
          ParticipantIODataDto.builder()
              .participant(participantsById.get(participantIOData.getParticipantId()).toDto())
              .batches(IODataDto.builder()
                  .rejected(participantIOData.getBatches().getRejected())
                  .submitted(participantIOData.getBatches().getSubmitted())
                  .build()
              )
              .transactions(IODataDto.builder()
                  .rejected(participantIOData.getTransactions().getRejected())
                  .submitted(participantIOData.getTransactions().getSubmitted())
                  .build()
              )
              .files(IODataDto.builder()
                  .rejected(participantIOData.getFiles().getRejected())
                  .submitted(participantIOData.getFiles().getSubmitted())
                  .build()
              )
              .build());
    }

    totalBatchesRejected = totalBatchesRejected / (double) participants.size();
    totalTransactionsRejected = totalTransactionsRejected / (double) participants.size();
    totalFilesRejected = totalFilesRejected / (double) participants.size();

    return IODashboardDto
        .builder()
        .dateFrom(date)
        .filesRejected(String.format("%.2f", totalFilesRejected))
        .batchesRejected(String.format("%.2f", totalBatchesRejected))
        .transactionsRejected(String.format("%.2f", totalTransactionsRejected))
        .rows(participantIODataDtos)
        .build();
  }

  private PositionDetailsTotalsDto countPositionTotals(List<ParticipantPositionDto> participantPosition) {
    return PositionDetailsTotalsDto.builder()
        .totalCredit(participantPosition.stream()
            .map(ParticipantPositionDto::getCredit)
            .filter(Objects::nonNull)
            .reduce(BigInteger::add).orElse(BigInteger.ZERO))
        .totalDebit(participantPosition.stream()
            .map(ParticipantPositionDto::getDebit)
            .filter(Objects::nonNull)
            .reduce(BigInteger::add).orElse(BigInteger.ZERO))
        .build();
  }

  private IntraDayPositionTotalDto countIntraDayTotals(List<IntraDayPositionGross> intraDays) {
    return IntraDayPositionTotalDto.builder()
        .totalDebitCap(intraDays.stream()
            .map(IntraDayPositionGross::getDebitCap)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
        .totalDebitPosition(intraDays.stream()
            .map(IntraDayPositionGross::getDebitPosition)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
        .build();
  }

  @Override
  public IODetailsDto presentIoDetails(Participant participant,
      IODetails ioDetails, LocalDate date) {
    return MAPPER.toDto(ioDetails, participant, date);
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }
}
