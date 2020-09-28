package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.ParticipantPosition;
import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.IOData;
import com.vocalink.crossproduct.ui.dto.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.SelfFundingSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.TotalPositionDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
@Slf4j
public class UIPresenter implements Presenter {

  private final SelfFundingSettlementDetailsMapper selfFundingDetailsMapper;

  @Override
  public SettlementDashboardDto presentSettlement(String context, List<Cycle> cycles,
      List<Participant> participants) {

    if (cycles.size() < 2) {
      throw new RuntimeException("Expected at least two cycles!");
    }

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
        .currentCycle(currentCycle.toDto())
        .previousCycle(previousCycle.toDto())
        .build();
  }

  @Override
  public SelfFundingSettlementDetailsDto presentSelfFundingSettlementDetails(
      String context, List<Cycle> cycles, List<PositionDetails> positionsDetails,
      Participant participant) {

    if (cycles.size() == 1) {
      return selfFundingDetailsMapper
          .presentOneCycleSelfFundingSettlementDetails(cycles, positionsDetails, participant);
    } else {
      return selfFundingDetailsMapper
          .presentFullSelfFundingSettlementDetails(cycles, positionsDetails, participant);
    }
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
              .batches(IOData.builder()
                  .rejected(participantIOData.getBatches().getRejected())
                  .submitted(participantIOData.getBatches().getSubmitted())
                  .build()
              )
              .transactions(IOData.builder()
                  .rejected(participantIOData.getTransactions().getRejected())
                  .submitted(participantIOData.getTransactions().getSubmitted())
                  .build()
              )
              .files(IOData.builder()
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
  public ClientType getClientType() {
    return ClientType.UI;
  }
}
