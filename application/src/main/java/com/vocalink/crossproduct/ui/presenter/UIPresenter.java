package com.vocalink.crossproduct.ui.presenter;

import static com.vocalink.crossproduct.domain.participant.ParticipantType.SCHEME_OPERATOR;
import static com.vocalink.crossproduct.ui.aspects.AuditAspect.RESPONSE_FAILURE;
import static com.vocalink.crossproduct.ui.aspects.AuditAspect.RESPONSE_SUCCESS;
import static com.vocalink.crossproduct.ui.aspects.EventType.AMEND_PARTICIPANT_CONFIG;
import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.SPACE;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.configuration.Configuration;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.io.IODashboard;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.permission.UIPermission;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference.ReasonCode;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference.Validation;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.ScheduleDayDetails;
import com.vocalink.crossproduct.domain.settlement.SettlementDetails;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.ui.aspects.ContentUtils;
import com.vocalink.crossproduct.ui.aspects.EventType;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.ParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.UserDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
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
import com.vocalink.crossproduct.ui.exceptions.UILayerException;
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper;
import java.io.InputStream;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UIPresenter implements Presenter {

  public static final String HYPHEN_DELIMITER = "-";

  @Value("${app.audit.service-id.prefix}")
  private String serviceIdPrefix;

  private final ContentUtils contentUtils;

  private final Clock clock;

  private final List<String> weekends = unmodifiableList(asList("Saturday", "Sunday"));
  private final List<String> rejectedStatuses = unmodifiableList(
      asList("NAK", "PRE-RJCT", "POST-RJCT"));

  @Override
  public SettlementDashboardDto presentAllParticipantsSettlement(List<Cycle> cycles,
      List<Participant> participants) {

    Cycle currentCycle = getCurrentCycle(cycles);
    Cycle previousCycle = getPreviousCycle(cycles);

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

    Cycle currentCycle = getCurrentCycle(cycles);
    Cycle previousCycle = getPreviousCycle(cycles);

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

    final Cycle currentCycle = getCurrentCycle(cycles);
    final Cycle previousCycle = getPreviousCycle(cycles);

    final ParticipantPosition currentPosition = getPosition(positions, currentCycle);
    final ParticipantPosition previousPosition = getPosition(positions, previousCycle);

    return MAPPER
        .toDto(currentCycle, previousCycle, currentPosition, previousPosition, participant);
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto presentFundedParticipantSettlementDetails(
      List<Cycle> cycles, List<ParticipantPosition> positions, Participant participant,
      Participant fundingParticipant, IntraDayPositionGross intradayPositionGross) {

    final Cycle currentCycle = getCurrentCycle(cycles);
    final Cycle previousCycle = getPreviousCycle(cycles);

    final ParticipantPosition currentPosition = getPosition(positions, currentCycle);
    final ParticipantPosition previousPosition = getPosition(positions, previousCycle);

    return MAPPER.toDto(currentCycle, previousCycle, currentPosition, previousPosition,
        participant, fundingParticipant, intradayPositionGross);
  }

  private ParticipantPosition getPosition(List<ParticipantPosition> positions, Cycle cycle) {
    if (cycle.isEmpty()) {
      return ParticipantPosition.builder().build();
    }
    return positions.stream()
        .filter(f -> f.getCycleId().equalsIgnoreCase(cycle.getId()))
        .findFirst()
        .orElse(ParticipantPosition.builder().build());
  }

  public Cycle getCurrentCycle(List<Cycle> cycles) {
    Cycle empty = Cycle.builder().build();
    if (Objects.isNull(cycles) || cycles.isEmpty()) {
      return empty;
    }
    Cycle currentCycle = cycles.get(0);
    if (cycles.size() == 1) {
      return currentCycle;
    }
    Cycle previousCycle = cycles.get(1);
    if (currentCycle.isInEodSodPeriod(previousCycle)) {
      return empty;
    }
    return currentCycle;
  }

  public Cycle getPreviousCycle(List<Cycle> cycles) {
    Cycle empty = Cycle.builder().build();
    if (cycles == null || cycles.isEmpty() || cycles.size() == 1) {
      return empty;
    }
    Cycle previousCycle = cycles.get(1);
    if (previousCycle == null) {
      return empty;
    }
    return previousCycle;
  }

  @Override
  public IODashboardDto presentInputOutput(List<Participant> participants,
      IODashboard ioDashboard, LocalDate date) {
    return MAPPER.toDto(ioDashboard, participants, date);
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
    return messageDirectionReferences.stream()
        .map(MAPPER::toDto)
        .collect(toList());
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
  public FileDetailsDto presentFileDetails(File file, Participant sender, Participant receiver) {
    return MAPPER.toDto(file, sender, receiver);
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
  public BatchDetailsDto presentBatchDetails(Batch batch) {
    return MAPPER.toDetailsDto(batch);
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(
      Page<SettlementDetails> settlementDetails,
      List<Participant> participants, Participant participant) {
    return MAPPER.toDto(settlementDetails, participants, participant);
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(
      Page<SettlementDetails> settlementDetails,
      List<Participant> participants, Participant participant, Participant settlementBank) {
    return MAPPER.toDto(settlementDetails, participants, participant, settlementBank);
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
      Validation validation, List<String> statuses) {
    return statuses.stream()
        .map(status -> ReasonCodeReferenceDto.builder()
            .enquiryType(validation.getValidationLevel())
            .hasReason(rejectedStatuses.contains(status))
            .reasonCodes(
                rejectedStatuses.contains(status) ? validation.getReasonCodes()
                    .stream()
                    .map(ReasonCode::getReasonCode)
                    .collect(toList()) : emptyList()
            )
            .status(status)
            .build()
        ).collect(toList());
  }

  @Override
  public SettlementScheduleDto presentSchedule(SettlementSchedule schedule) {
    final List<SettlementCycleScheduleDto> weekdayCycles = schedule.getScheduleDayDetails().stream()
        .filter(s -> !weekends.contains(s.getWeekDay()))
        .map(ScheduleDayDetails::getCycles)
        .flatMap(List::stream)
        .map(MAPPER::toDto)
        .collect(toList());
    final List<SettlementCycleScheduleDto> weekendCycles = schedule.getScheduleDayDetails().stream()
        .filter(s -> weekends.contains(s.getWeekDay()))
        .map(ScheduleDayDetails::getCycles)
        .flatMap(List::stream)
        .map(MAPPER::toDto)
        .collect(toList());

    return new SettlementScheduleDto(weekdayCycles, weekendCycles, schedule.getUpdatedAt());
  }

  @Override
  public TransactionDetailsDto presentTransactionDetails(Transaction transaction) {
    return MAPPER.toDetailsDto(transaction);
  }

  @Override
  public ApprovalDetailsDto presentApprovalDetails(Approval approval,
      List<Participant> participants) {
    return MAPPER.toDto(approval, participants);
  }

  @Override
  public PageDto<ManagedParticipantDto> presentManagedParticipants(
      Page<Participant> participants, Map<String, Approval> approvals) {
    final Map<String, String> fundingParticipants = participants.getItems().stream()
        .filter(participant -> participant.getFundingBic().equals("NA"))
        .collect(toMap(Participant::getId, Participant::getName));
    return MAPPER.toDto(participants, approvals, fundingParticipants);
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
  public PageDto<ApprovalDetailsDto> presentApproval(Page<Approval> approvals,
      List<Participant> participants) {
    return MAPPER.toApprovalDetailsDto(approvals, participants);
  }

  @Override
  public PageDto<RoutingRecordDto> presentRoutingRecords(Page<RoutingRecord> routingRecords) {
    return MAPPER.toDto(routingRecords, RoutingRecordDto.class);
  }

  @Override
  public ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Participant fundingParticipant, Account account,
      Map<String, Approval> approvals) {
    return MAPPER.toDto(participant, configuration, fundingParticipant, account, approvals);
  }

  @Override
  public ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Account account, Map<String, Approval> approvals) {
    return MAPPER.toDto(participant, configuration, account, approvals);
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
      Integer dataRetentionDays, String timeZone) {
    return MAPPER.toDto(configuration, dataRetentionDays, timeZone);
  }

  @Override
  public List<UserDetailsDto> presentUserDetails(List<AuditDetails> details) {
    return details.stream()
        .map(e -> UserDetailsDto.builder()
            .username(e.getUsername())
            .fullName(e.getFirstName() + SPACE + e.getLastName())
            .build())
        .collect(toList());
  }

  @Override
  public PageDto<AuditDto> presentAuditDetails(Page<AuditDetails> details) {
    final List<AuditDto> dtoDetails = details.getItems().stream()
        .map(MAPPER::toDto)
        .collect(toList());

    dtoDetails.forEach(d -> d.prefixServiceId(serviceIdPrefix + HYPHEN_DELIMITER));

    return new PageDto<>(details.getTotalResults(), dtoDetails);
  }

  @Override
  public List<String> presentEvents(List<String> events) {
    return events;
  }

  @Override
  public AuditDetailsDto presentAuditDetails(AuditDetails request, AuditDetails response,
      Participant participant) {
    final EventType eventType = getEventByDescription(request.getActivityName());

    Object requestContent = contentUtils
        .toObject(request.getContents(), eventType);

    Object responseContent = response.getContents();

    if (AMEND_PARTICIPANT_CONFIG.equals(eventType) && !RESPONSE_FAILURE.equals(responseContent)) {
      responseContent = contentUtils.toObject(response.getContents(), eventType);
      requestContent = getRequestContent(requestContent, responseContent);
    }

    final ParticipantDetailsDto participantDto = new ParticipantDetailsDto(
        participant.getBic(), participant.getName());

    final UserDetailsDto userDto = new UserDetailsDto(request.getUsername(),
        request.getFirstName() + SPACE + request.getLastName());

    return AuditDetailsDto.builder()
        .serviceId(serviceIdPrefix + HYPHEN_DELIMITER + request.getServiceId().toString())
        .eventType(eventType.name())
        .product(request.getIpsSuiteApplicationName())
        .entity(participantDto)
        .user(userDto)
        .customer(request.getCustomer())
        .request(requestContent)
        .requestDate(request.getTimestamp())
        .response(getResponseContent(responseContent))
        .responseDate(response.getTimestamp())
        .approvalRequestId(response.getApprovalRequestId())
        .build();
  }

  private EventType getEventByDescription(String activity) {
    return Stream.of(EventType.values())
        .filter(v -> v.name().equalsIgnoreCase(activity))
        .findFirst()
        .orElseThrow(() -> new UILayerException("Event type not found for activity: " + activity));
  }

  private Object getRequestContent(Object request, Object response) {
    final Map mapRequest = contentUtils.toMap(request);
    final Map mapResponse = contentUtils.toMap(response);

    mapResponse.forEach(
        (key, value) -> mapRequest.merge(
            key, value, (v1, v2) -> v1.equals(v2) ? v1 : v1 + ", " + v2
        )
    );
    return mapRequest;
  }

  private Object getResponseContent(Object response) {
    return RESPONSE_FAILURE.equals(response) ? RESPONSE_FAILURE : RESPONSE_SUCCESS;
  }

  @Override
  public List<ApprovalUserDto> presentRequestedDetails(List<UserDetails> userDetails) {
    return userDetails.stream()
        .map(MAPPER::toDto)
        .collect(toList());
  }

  @Override
  public CurrentUserInfoDto presentCurrentUserInfo(Participant participant,
      List<UIPermission> uiPermissions, AuditDetails auditDetails) {
    final List<String> permissions = uiPermissions.stream()
        .map(UIPermission::getKey)
        .collect(toList());
    return MAPPER.toDto(participant, permissions, auditDetails);
  }
}
