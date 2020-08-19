package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.CycleRepository;
import com.vocalink.crossproduct.domain.CycleStatus;
import com.vocalink.crossproduct.domain.ParticipantPosition;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.shared.cycle.CyclesClient;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class CycleRepositoryAdapter implements CycleRepository {

  private final ClientFactory clientFactory;

  @Override
  public List<Cycle> findAll(String context) {
    CyclesClient cyclesClient = clientFactory.getCyclesClient(context);

    log.info("Fetching all cycles from context {} ... ", context);

    return cyclesClient.findCycles()
        .stream()
        .map(cycleDto ->
            Cycle.builder()
                .id(cycleDto.getId())
                .cutOffTime(cycleDto.getCutOffTime())
                .settlementTime(cycleDto.getSettlementTime())
                .status(CycleStatus.valueOf(cycleDto.getStatus()))
                .positions(cycleDto.getPositions().stream()
                    .map(positionDto ->
                        ParticipantPosition.builder()
                            .credit(positionDto.getCredit())
                            .debit(positionDto.getDebit())
                            .netPosition(positionDto.getNetPosition())
                            .participantId(positionDto.getParticipantId())
                            .build()
                    ).collect(Collectors.toList())
                )
                .build()
        )
        .collect(Collectors.toList());
  }
}
