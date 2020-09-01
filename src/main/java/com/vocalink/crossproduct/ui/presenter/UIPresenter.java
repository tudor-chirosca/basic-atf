package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.IORejectedStats;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantIOData;
import com.vocalink.crossproduct.domain.ParticipantPosition;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.IOData;
import com.vocalink.crossproduct.ui.dto.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.SettlementPositionDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UIPresenter implements Presenter {

  @Override
  public SettlementDashboardDto presentSettlement(String context, List<Cycle> cycles,
      List<Participant> participants) {
    if (cycles.size() != 2) {
      throw new RuntimeException("Expected at least two cycles!");
    }

    cycles.sort(Comparator.comparing(Cycle::getId).reversed());

    Cycle currentCycle = cycles.get(0);
    Cycle previousCycle = cycles.get(1);

    Map<String, ParticipantPosition> positionsCurrentCycle = currentCycle.getPositions()
        .stream()
        .collect(Collectors.toMap(ParticipantPosition::getParticipantId, Function.identity()));

    Map<String, ParticipantPosition> positionsPreviousCycle = previousCycle.getPositions()
        .stream()
        .collect(Collectors.toMap(ParticipantPosition::getParticipantId, Function.identity()));

    List<SettlementPositionDto> settlementPositionDtos = new ArrayList<>();
    for (Participant participant : participants) {
      settlementPositionDtos.add(
          SettlementPositionDto.builder()
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
  public IODashboardDto presentInputOutput(List<Participant> participants,
      List<ParticipantIOData> ioData,
      IORejectedStats ioRejectedStats) {

    Map<String, Participant> participantsById = participants.stream().collect(
        Collectors.toMap(Participant::getId, Function.identity()));

    List<ParticipantIODataDto> participantIODataDtos = ioData.stream()
        .map(ioDataDto ->
            ParticipantIODataDto.builder()
                .participant(participantsById.get(ioDataDto.getParticipantId()).toDto())
                .batches(IOData.builder()
                    .rejected(ioDataDto.getBatches().getRejected())
                    .submitted(ioDataDto.getBatches().getSubmitted())
                    .build()
                )
                .transactions(IOData.builder()
                    .rejected(ioDataDto.getTransactions().getRejected())
                    .submitted(ioDataDto.getTransactions().getSubmitted())
                    .build()
                )
                .files(IOData.builder()
                    .rejected(ioDataDto.getFiles().getRejected())
                    .submitted(ioDataDto.getFiles().getSubmitted())
                    .build()
                )
                .build())
        .collect(Collectors.toList());

    return IODashboardDto
        .builder()
        .datetime(LocalDateTime.now().toString())
        .filesRejected(ioRejectedStats.getFilesRejected())
        .batchesRejected(ioRejectedStats.getBatchesRejected())
        .transactionsRejected(ioRejectedStats.getTransactionsRejected())
        .rows(participantIODataDtos)
        .build();
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }
}
