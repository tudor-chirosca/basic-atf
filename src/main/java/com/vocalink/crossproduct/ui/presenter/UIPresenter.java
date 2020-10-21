package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODataDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.presenter.mapper.CycleMapper;
import com.vocalink.crossproduct.ui.presenter.mapper.IODetailsMapper;
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class UIPresenter implements Presenter {

  private final SelfFundingSettlementDetailsMapper selfFundingDetailsMapper;

  @Override
  public SettlementDashboardDto presentSettlement(List<Cycle> cycles,
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
              .currentPosition(positionsCurrentCycle.get(participant.getId()).toDto())
              .previousPosition(positionsPreviousCycle.get(participant.getId()).toDto())
              .participant(participant)
              .build()
      );
    }

    return SettlementDashboardDto.builder()
        .positions(settlementPositionDtos)
        .currentCycle(CycleMapper.map(currentCycle))
        .previousCycle(CycleMapper.map(previousCycle))
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

  @Override
  public IODetailsDto presentIoDetails(Participant participant,
      IODetails ioDetails, LocalDate date) {
    return IODetailsMapper.map(ioDetails, participant, date);
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }
}
