package com.vocalink.portal.application;

import com.vocalink.portal.domain.Cycle;
import com.vocalink.portal.domain.CycleRepository;
import com.vocalink.portal.domain.Participant;
import com.vocalink.portal.domain.ParticipantRepository;
import com.vocalink.portal.domain.ParticipantPosition;
import com.vocalink.portal.domain.SettlementPosition;
import com.vocalink.portal.ui.dto.SettlementDto;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PositionsService {

  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;

  public SettlementDto getSettlement() {
    log.info("Fetching positions...");

    List<Participant> participants = participantRepository.fetchParticipants();
    List<Cycle> cycles = cycleRepository.fetchCycles();

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

    List<SettlementPosition> settlementPositions = new ArrayList<>();
    for (Participant participant : participants) {
      settlementPositions.add(
          SettlementPosition.builder()
              .currentPosition(positionsCurrentCycle.get(participant.getId()).toDto())
              .previousPosition(positionsPreviousCycle.get(participant.getId()).toDto())
              .participant(participant)
              .build()
      );
    }

    return SettlementDto.builder()
        .positions(settlementPositions)
        .currentCycle(currentCycle.toDto())
        .previousCycle(previousCycle.toDto())
        .build();
  }
}

