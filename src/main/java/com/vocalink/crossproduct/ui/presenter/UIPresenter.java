package com.vocalink.crossproduct.ui.presenter;

import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;
import static java.util.stream.Collectors.toList;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.io.IODataDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UIPresenter implements Presenter {

  @Value("${default.message.reference.name}")
  private String defaultMessageReferenceName;

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

    List<TotalPositionDto> positionsDto = participants.stream()
        .map(participant ->
            MAPPER.toDto(participant, currentCycle, previousCycle, participant.getBic()))
        .collect(toList());

    return MAPPER.toDto(currentCycle, previousCycle, positionsDto);
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

    List<TotalPositionDto> positionsDto = participants.stream()
        .map(participant ->
            MAPPER.toDto(participant, currentCycle, previousCycle, intraDays, participant.getBic()))
        .collect(toList());

    return MAPPER.toDto(currentCycle, previousCycle, positionsDto, fundingParticipant, intraDays);
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
    return MAPPER.toDto(ioDetails, participant, date);
  }

  @Override
  public AlertReferenceDataDto presentAlertReference(AlertReferenceData alertsReference) {
    return MAPPER.toDto(alertsReference);
  }

  @Override
  public AlertStatsDto presentAlertStats(AlertStats alertStats) {
    return MAPPER.toDto(alertStats);
  }

  @Override
  public List<AlertDto> presentAlert(List<Alert> alerts) {
    return alerts.stream()
        .map(MAPPER::toDto)
        .collect(toList());
  }

  @Override
  public List<MessageDirectionReferenceDto> presentMessageDirectionReferences(
      List<MessageDirectionReference> messageDirectionReferences) {
    List<MessageDirectionReferenceDto> messagesDto = messageDirectionReferences.stream()
        .map(MAPPER::toDto)
        .collect(toList());
    return setDefault(messagesDto);
  }

  private List<MessageDirectionReferenceDto> setDefault(
      List<MessageDirectionReferenceDto> messages) {
    return messages.stream()
        .map(message -> {
          if (message.getName() != null && message.getName().equals(defaultMessageReferenceName)) {
            message.setDefault(true);
          }
          return message;
        })
        .collect(toList());
  }

  @Override
  public List<ParticipantReferenceDto> presentParticipantReferences(List<Participant> participants) {
    return participants.stream()
        .map(p -> new ParticipantReferenceDto(p.getBic(), p.getName()))
        .sorted(Comparator.comparing(ParticipantReferenceDto::getName))
        .collect(Collectors.toList());
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }

  @Override
  public List<FileStatusesDto> presentFileReferences(List<FileReference> fileReferences) {
    return MAPPER.toDto(fileReferences);
  }
}
