package com.vocalink.portal.application;

import com.vocalink.portal.domain.Cycle;
import com.vocalink.portal.domain.CycleRepository;
import com.vocalink.portal.domain.Participant;
import com.vocalink.portal.domain.ParticipantRepository;
import com.vocalink.portal.domain.Position;
import com.vocalink.portal.domain.PositionItemFactory;
import com.vocalink.portal.ui.dto.PositionItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class PositionsService {

  private final ParticipantRepository participantRepository;
  private final CycleRepository cycleRepository;

  public List<PositionItem> fetchPositions() {
    log.info("Fetching positions...");

    Mono<Participant[]> participantMono = participantRepository.fetchParticipants();
    Mono<Cycle[]> cyclesMono = cycleRepository.fetchCycles();

    return Mono.zip(participantMono, cyclesMono,

        (participants, cycles) -> {
          if (cycles.length != 2) {
            throw new RuntimeException("Expected two cycles!");
          }

          Arrays.sort(cycles, Comparator.comparing(Cycle::getId));

          log.info("Positions retrieved...");

          Cycle currentCycle = cycles[0];
          Cycle previousCycle = cycles[1];

          Map<String, Position> positionsCurrentCycle = currentCycle.getPositions()
              .stream()
              .collect(Collectors.toMap(Position::getParticipantId, Function.identity()));

          Map<String, Position> positionsPreviousCycle = previousCycle.getPositions()
              .stream()
              .collect(Collectors.toMap(Position::getParticipantId, Function.identity()));

          List<PositionItem> positionItems = new ArrayList<>();
          for (Participant participant : participants) {
            positionItems.add(
                PositionItemFactory.newInstance(positionsCurrentCycle.get(participant.getId()),
                    positionsPreviousCycle.get(participant.getId()), participant));
          }
          return positionItems;
        }).block();
  }
}

