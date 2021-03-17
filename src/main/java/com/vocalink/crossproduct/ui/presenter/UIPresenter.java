package com.vocalink.crossproduct.ui.presenter;

import static com.vocalink.crossproduct.domain.participant.ParticipantType.SCHEME_OPERATOR;
import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.configuration.Configuration;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeValidation;
import com.vocalink.crossproduct.domain.reference.ReasonCodeValidation.ReasonCode;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODataDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementCycleScheduleDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UIPresenter implements Presenter {

  @Value("${default.message.reference.name}")
  private String defaultMessageReferenceName;

  private final String PREVIOUS_CYCLE = "PREVIOUS";
  private final String CURRENT_CYCLE = "CURRENT";
  private final List<String> weekends = unmodifiableList(asList("Saturday", "Sunday"));
  private final List<String> rejectedStatuses =
      unmodifiableList(asList("NAK", "PRE-RJCT", "POST-RJCT"));

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

    return MAPPER
        .toDto(currentCycle, previousCycle, currentPosition, previousPosition, participant);
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

  private Cycle getCycle(List<Cycle> cycles, String cycleOrder) {
    // Cycles are always in descending order
    if (cycleOrder.equals(CURRENT_CYCLE)) {
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
  public IODetailsDto presentIoDetails(Participant participant, IODetails ioDetails,
      LocalDate date) {
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
    return MAPPER.toDto(alerts, AlertDto.class);
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
  public PageDto<FileDto> presentFiles(Integer totalResults, List<File> items) {
    final List<FileDto> files = items.stream()
        .map(MAPPER::toDto)
        .collect(toList());
    return new PageDto<>(totalResults, files);
  }

  @Override
  public List<DayCycleDto> presentCycleDateReferences(List<DayCycle> dayCycles) {
    return dayCycles.stream()
        .map(MAPPER::toDto)
        .collect(toList());
  }

  @Override
  public FileDetailsDto presentFileDetails(File file, Participant participant, Account account) {
    final EnquirySenderDetailsDto sender = EnquirySenderDetailsDto.builder()
        .entityName(participant.getName())
        .entityBic(participant.getBic())
        .iban(account.getIban())
        .fullName(StringUtils.EMPTY)
        .build();

    return FileDetailsDto.builder()
        .fileName(file.getFileName())
        .nrOfBatches(file.getNrOfBatches())
        .fileSize(file.getFileSize())
        .settlementDate(file.getSettlementDate().toLocalDate())
        .settlementCycleId(file.getSettlementCycle())
        .createdAt(file.getCreatedDate())
        .status(file.getStatus())
        .reasonCode(file.getReasonCode())
        .messageType(file.getMessageType())
        .sender(sender)
        .build();
  }

  @Override
  public PageDto<BatchDto> presentBatches(Integer totalResults, List<Batch> items) {
    final List<BatchDto> batches = items.stream().map(MAPPER::toDto).collect(toList());
    return new PageDto<>(totalResults, batches);
  }

  @Override
  public PageDto<TransactionDto> presentTransactions(Integer totalResults,
      List<Transaction> items) {
    final List<TransactionDto> files = items.stream()
        .map(MAPPER::toDto)
        .collect(toList());
    return new PageDto<>(totalResults, files);
  }

  @Override
  public BatchDetailsDto presentBatchDetails(Batch batch, File file) {
    return MAPPER.toDetailsDto(batch, file);
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(ParticipantSettlement settlement,
      List<Participant> participants, Participant participant) {
    return MAPPER.toDto(settlement, participants, participant);
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(ParticipantSettlement settlement,
      List<Participant> participants, Participant participant, Participant settlementBank) {
    return MAPPER.toDto(settlement, participants, participant, settlementBank);
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
      List<Participant> participants) {
    return participants.stream()
        .map(MAPPER::toReferenceDto)
        .sorted(comparing((ParticipantReferenceDto p) -> !p.getParticipantType()
            .equals(SCHEME_OPERATOR.getDescription()))
            .thenComparing(ParticipantReferenceDto::getName))
        .collect(toList());
  }

  @Override
  public ClientType getClientType() {
    return ClientType.UI;
  }

  @Override
  public List<ReasonCodeReferenceDto> presentReasonCodeReferences(
      ReasonCodeValidation reasonCodeValidation, List<String> statuses) {
    return statuses.stream()
        .map(status -> ReasonCodeReferenceDto.builder()
            .enquiryType(reasonCodeValidation.getValidationLevel().name())
            .hasReason(rejectedStatuses.contains(status))
            .reasonCodes(
                rejectedStatuses.contains(status) ? reasonCodeValidation.getReasonCodes()
                    .stream()
                    .map(ReasonCode::getReasonCode)
                    .collect(toList()) : emptyList()
            )
            .status(status)
            .build()
        ).collect(toList());
  }

  @Override
  public SettlementScheduleDto presentSchedules(List<SettlementSchedule> schedules) {
    final List<SettlementCycleScheduleDto> weekdayCycles = schedules.stream()
        .filter(s -> !weekends.contains(s.getWeekDay()))
        .map(SettlementSchedule::getCycles)
        .flatMap(List::stream)
        .map(MAPPER::toDto)
        .collect(toList());
    final List<SettlementCycleScheduleDto> weekendCycles = schedules.stream()
        .filter(s -> weekends.contains(s.getWeekDay()))
        .map(SettlementSchedule::getCycles)
        .flatMap(List::stream)
        .map(MAPPER::toDto)
        .collect(toList());

    return new SettlementScheduleDto(weekdayCycles, weekendCycles);
  }

  @Override
  public TransactionDetailsDto presentTransactionDetails(Transaction transaction) {
    return MAPPER.toDetailsDto(transaction);
  }

  @Override
  public ApprovalDetailsDto presentApprovalDetails(Approval approval) {
    return MAPPER.toDto(approval);
  }

  @Override
  public PageDto<ManagedParticipantDto> presentManagedParticipants(
      Page<Participant> participants) {
    return MAPPER.toDto(participants, ManagedParticipantDto.class);
  }

  @Override
  public PageDto<BroadcastDto> presentBroadcasts(int totalResults, List<BroadcastDto> items) {
    return new PageDto<>(totalResults, items);
  }

  @Override
  public BroadcastDto presentBroadcast(Broadcast broadcast,
      List<Participant> references) {
    final List<ParticipantReferenceDto> referenceDtos = references.stream()
        .map(MAPPER::toReferenceDto)
        .collect(toList());

    final BroadcastDto broadcastDto = DTOMapper.MAPPER.toDto(broadcast);
    broadcastDto.setRecipients(referenceDtos);

    return broadcastDto;
  }

  @Override
  public PageDto<ApprovalDetailsDto> presentApproval(Page<Approval> approvals) {
    return MAPPER.toDto(approvals, ApprovalDetailsDto.class);
  }

  @Override
  public PageDto<RoutingRecordDto> presentRoutingRecords(Page<RoutingRecord> routingRecords) {
    return MAPPER.toDto(routingRecords, RoutingRecordDto.class);
  }

  @Override
  public ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Participant fundingParticipant, Account account) {
    return MAPPER.toDto(participant, configuration, fundingParticipant, account);
  }

  @Override
  public ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Account account) {
    return MAPPER.toDto(participant, configuration, account);
  }

  @Override
  public PageDto<ReportDto> presentReports(Page<Report> reports) {
    return MAPPER.toDto(reports, ReportDto.class);
  }

  @Override
  public Resource presentStream(InputStream is) {
    return new InputStreamResource(is);
  }

  @Override
  public ApprovalConfirmationResponseDto presentApprovalResponse(
      ApprovalConfirmationResponse response) {
    return MAPPER.toDto(response);
  }

  @Override
  public ValidationApprovalDto presentApprovalValidation(ValidationApproval response) {
    return MAPPER.toDto(response);
  }

  @Override
  public ConfigurationDto presentConfiguration(Configuration configuration,
      Integer dataRetentionDays) {
    return MAPPER.toDto(configuration, dataRetentionDays);
  }
}
