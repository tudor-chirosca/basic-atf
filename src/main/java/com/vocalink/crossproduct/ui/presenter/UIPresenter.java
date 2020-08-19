package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantPosition;
import com.vocalink.crossproduct.ui.dto.SettlementPositionDto;
import com.vocalink.crossproduct.ui.dto.SettlementDto;
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
  public SettlementDto presentSettlement(String context, List<Cycle> cycles,
      List<Participant> participants) {
    if (cycles.size() != 2) {
      throw new RuntimeException("Expected two cycles!");
    }

    cycles.sort(Comparator.comparing(Cycle::getId).reversed());

    log.info("Positions retrieved...");

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

    return SettlementDto.builder()
        .positions(settlementPositionDtos)
        .currentCycle(currentCycle.toDto())
        .previousCycle(previousCycle.toDto())
        .build();
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }
}
