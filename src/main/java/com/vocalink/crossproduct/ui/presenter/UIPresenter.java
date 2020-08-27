package com.vocalink.crossproduct.ui.presenter;

import com.github.javafaker.Faker;
import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantPosition;
import com.vocalink.crossproduct.domain.ParticipantStatus;
import com.vocalink.crossproduct.ui.dto.InputOutputDataDto;
import com.vocalink.crossproduct.ui.dto.InputOutputItemDto;
import com.vocalink.crossproduct.ui.dto.InputOutputDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.SettlementDto;
import com.vocalink.crossproduct.ui.dto.SettlementPositionDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
  private final Faker faker = new Faker();
  public List<Participant> participants = Arrays.asList(
      Participant
          .builder()
          .id("NDEASESS")
          .bic("NDEASESS")
          .name("Nordea bank")
          .status(ParticipantStatus.SUSPENDED)
          .suspendedTime(LocalDateTime.now())
          .build(),
      Participant
          .builder()
          .id("HANDSESS")
          .bic("HANDSESS")
          .name("Svenska Handelsbanken")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("ESSESESS")
          .bic("ESSESESS")
          .name("SEB Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("SWEDSESS")
          .bic("SWEDSESS")
          .name("Swedbank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("FORXSES1")
          .bic("FORXSES1")
          .name("Forex bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("NNSESES1")
          .bic("NNSESES1")
          .name("Nordnet Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(LocalDateTime.now())
          .build(),
      Participant
          .builder()
          .id("IKANSE21")
          .bic("IKANSE21")
          .name("Ikano bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("AVANSESX")
          .bic("AVANSESX")
          .name("Avanza bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("IBCASES1")
          .bic("IBCASES1")
          .name("ICA Banken")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("RESUSE21")
          .bic("RESUSE21")
          .name("Resursbank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("MONYSES1")
          .bic("MONYSES1")
          .name("Ge Money Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("MEEOSES1")
          .bic("MEEOSES1")
          .name("Meetoo")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("SCADSE21")
          .bic("SCADSE21")
          .name("Scandem")
          .status(ParticipantStatus.SUSPENDED)
          .suspendedTime(LocalDateTime.now())
          .build(),
      Participant
          .builder()
          .id("SWCTSES1")
          .bic("SWCTSES1")
          .name("Swedish Capital Trust")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("CARNSES1")
          .bic("CARNSES1")
          .name("Carnegie Investment Bank")
          .status(ParticipantStatus.SUSPENDED)
          .suspendedTime(LocalDateTime.now())
          .build(),
      Participant
          .builder()
          .id("LAHYSESS")
          .bic("LAHYSESS")
          .name("Landshypotek Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("MEMMSE21")
          .bic("MEMMSE21")
          .name("Medmera Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("SWEDSES1")
          .bic("SWEDSES1")
          .name("Swedbank 2")
          .status(ParticipantStatus.SUSPENDED)
          .suspendedTime(LocalDateTime.now())
          .build(),
      Participant
          .builder()
          .id("SVEASES1")
          .bic("SVEASES1")
          .name("Svea Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build(),
      Participant
          .builder()
          .id("ELLFSESS")
          .bic("ELLFSESS")
          .name("Lansfosakringar Bank")
          .status(ParticipantStatus.ACTIVE)
          .suspendedTime(null)
          .build()
  );


  @Override
  public SettlementDto presentSettlement(String context, List<Cycle> cycles,
      List<Participant> participants) {
    if (cycles.size() != 2) {
      throw new RuntimeException("Expected two cycles!");
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

    return SettlementDto.builder()
        .positions(settlementPositionDtos)
        .currentCycle(currentCycle.toDto())
        .previousCycle(previousCycle.toDto())
        .build();
  }

  @Override
  public InputOutputDto presentInputOutput() {
    List<InputOutputItemDto> rows = new ArrayList<>();
    for (int i = 0; i < participants.size(); i++) {
      InputOutputItemDto inputOutputItemDto = getIOItem();
      inputOutputItemDto.setParticipant(participants.get(i).toDto());
      rows.add(inputOutputItemDto);
    }
    return InputOutputDto
        .builder()
        .datetime(LocalDateTime.now().toString())
        .filesRejected(faker.number().randomDouble(2, 0, 4))
        .batchesRejected(faker.number().randomDouble(2, 0, 4))
        .transactionsRejected(faker.number().randomDouble(2, 0, 4))
        .rows(rows)
        .build();
  }

  private InputOutputItemDto getIOItem() {
    return InputOutputItemDto
        .builder()
        .files(InputOutputDataDto.builder()
            .rejected(faker.number().randomDouble(2, 0, 4))
            .submitted(Integer.parseInt(faker.number().digits(3)))
            .build()
        )
        .batches(InputOutputDataDto.builder()
            .rejected(faker.number().randomDouble(2, 0, 4))
            .submitted(Integer.parseInt(faker.number().digits(4)))
            .build()
        )
        .transactions(InputOutputDataDto.builder()
            .rejected(faker.number().randomDouble(2, 0, 4))
            .submitted(Integer.parseInt(faker.number().digits(5)))
            .build()
        )
        .build();
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }
}
