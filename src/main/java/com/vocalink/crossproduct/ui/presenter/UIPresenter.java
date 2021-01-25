package com.vocalink.crossproduct.ui.presenter;

import static com.vocalink.crossproduct.domain.participant.ParticipantType.SCHEME;
import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODataDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
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

  private String PREVIOUS_CYCLE = "PREVIOUS";
  private String CURRENT_CYCLE = "CURRENT";

  @Override
  public SettlementDashboardDto presentAllParticipantsSettlement(List<Cycle> cycles,
      List<Participant> participants) {

    Cycle currentCycle = getCycle(cycles, CURRENT_CYCLE);
    Cycle previousCycle = getCycle(cycles, PREVIOUS_CYCLE);

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

    Cycle currentCycle = getCycle(cycles, CURRENT_CYCLE);
    Cycle previousCycle = getCycle(cycles, PREVIOUS_CYCLE);

    List<TotalPositionDto> positionsDto = participants.stream()
        .map(participant ->
            MAPPER.toDto(participant, currentCycle, previousCycle, intraDays, participant.getBic()))
        .collect(toList());

    ParticipantDto fundingParticipantDto = MAPPER.toDto(fundingParticipant);

    return MAPPER
        .toDto(currentCycle, previousCycle, positionsDto, fundingParticipantDto, intraDays);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto presentParticipantSettlementDetails(
      List<Cycle> cycles, List<ParticipantPosition> positions, Participant participant) {

    Cycle currentCycle = getCycle(cycles, CURRENT_CYCLE);
    Cycle previousCycle = getCycle(cycles, PREVIOUS_CYCLE);

    ParticipantPosition currentPosition = getPosition(positions, currentCycle);
    ParticipantPosition previousPosition = getPosition(positions, previousCycle);

    return MAPPER.toDto(currentCycle, previousCycle, currentPosition, previousPosition, participant);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto presentFundedParticipantSettlementDetails(
      List<Cycle> cycles, List<ParticipantPosition> positions, Participant participant,
      Participant fundingParticipant, IntraDayPositionGross intradayPositionGross) {

    Cycle currentCycle = getCycle(cycles, CURRENT_CYCLE);
    Cycle previousCycle = getCycle(cycles, PREVIOUS_CYCLE);

    ParticipantPosition currentPosition = getPosition(positions, currentCycle);
    ParticipantPosition previousPosition = getPosition(positions, previousCycle);

    return MAPPER.toDto(currentCycle, previousCycle, currentPosition, previousPosition,
        participant, fundingParticipant, intradayPositionGross);
  }

  private ParticipantPosition getPosition(List<ParticipantPosition> positions, Cycle cycle) {
    return positions.stream()
        .filter(f -> f.getCycleId().equalsIgnoreCase(cycle.getId())).findFirst()
        .orElseThrow(() -> new NonConsistentDataException(
            "Current position does not match current cycle id")
        );
  }

  private Cycle getCycle(List<Cycle> cycles, String cycleType) {
    // Cycles are always in descending order
    if (cycleType.equals(CURRENT_CYCLE)) {
      return cycles.get(0);
    }
    return cycles.get(1);
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
              .participant(MAPPER.toDto(participantsById.get(participantIOData.getParticipantId())))
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
  public PageDto<AlertDto> presentAlert(Page<Alert> alerts) {
    return MAPPER.toAlertPageDto(alerts);
  }

  @Override
  public List<MessageDirectionReferenceDto> presentMessageDirectionReferences(
      List<MessageDirectionReference> messageDirectionReferences) {
    List<MessageDirectionReferenceDto> messagesDto = messageDirectionReferences.stream()
        .map(MAPPER::toDto)
        .collect(toList());
    return setDefaultDirection(messagesDto);
  }

  @Override
  public PageDto<FileDto> presentFiles(Page<File> enquiries) {
    return MAPPER.toFilePageDto(enquiries);
  }

  @Override
  public List<CycleDto> presentCycleDateReferences(List<Cycle> cycles) {
    return cycles.stream()
        .map(MAPPER::toDto)
        .collect(toList());
  }

  @Override
  public FileDetailsDto presentFileDetails(File file) {
    return MAPPER.toDetailsDto(file);
  }

  @Override
  public PageDto<BatchDto> presentBatches(Page<Batch> batches) {
    return MAPPER.toBatchPageDto(batches);
  }

  @Override
  public PageDto<TransactionDto> presentTransactions(Page<Transaction> transactions) {
    return MAPPER.toTransactionPageDto(transactions);
  }

  @Override
  public BatchDetailsDto presentBatchDetails(Batch batch) {
    return MAPPER.toDetailsDto(batch);
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(ParticipantSettlement settlement,
      List<Participant> participants) {
    return MAPPER.toDto(settlement, participants);
  }

  @Override
  public PageDto<ParticipantSettlementCycleDto> presentSettlements(
      Page<ParticipantSettlement> settlements, List<Participant> participants) {
    return MAPPER.toDto(settlements, participants);
  }

  @Override
  public LatestSettlementCyclesDto presentLatestCycles(
      List<Cycle> cycles) {
    List<CycleDto> localCycles = cycles.stream()
        .map(MAPPER::toDto)
        .collect(toList());

    assert localCycles.size() > 1;

    return LatestSettlementCyclesDto
        .builder()
        .previousCycle(localCycles.get(1))
        .currentCycle(localCycles.get(0))
        .build();
  }

  private List<MessageDirectionReferenceDto> setDefaultDirection(
      List<MessageDirectionReferenceDto> messages) {
    return messages.stream()
        .peek(message -> {
          if (message.getName() != null && message.getName().equals(defaultMessageReferenceName)) {
            message.setDefault(true);
          }
        })
        .collect(toList());
  }

  @Override
  public List<ParticipantReferenceDto> presentParticipantReferences(
      List<ParticipantReference> participants) {
    return participants.stream()
        .map(MAPPER::toDto)
        .sorted(Comparator.comparing((ParticipantReferenceDto p) -> !p.getParticipantType().equals(
            SCHEME.getDescription())).thenComparing(ParticipantReferenceDto::getName))
        .collect(toList());
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }

  @Override
  public List<FileStatusesTypeDto> presentFileReferencesFor(
      List<FileReference> fileReferences, String enquiryType) {
    List<FileReference> filteredReferences = fileReferences.stream()
        .filter(f -> f.getEnquiryType().equalsIgnoreCase(enquiryType))
        .collect(toList());
    return MAPPER.toDtoType(filteredReferences);
  }

  @Override
  public SettlementScheduleDto presentSchedule(SettlementSchedule schedule) {
    return MAPPER.toDto(schedule);
  }

  @Override
  public TransactionDetailsDto presentTransactionDetails(Transaction transaction,
      EnquirySenderDetails sender) {
    return MAPPER.toDetailsDto(transaction, sender);
  }

  @Override
  public TransactionDetailsDto presentTransactionDetails(Transaction transaction,
      EnquirySenderDetails sender, EnquirySenderDetails receiver) {
    return MAPPER.toDetailsDto(transaction, sender, receiver);
  }

}
