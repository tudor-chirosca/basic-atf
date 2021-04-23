package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static com.vocalink.crossproduct.infrastructure.bps.mappers.MapperUtils.getNameByType;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Amount;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertPriorityData;
import com.vocalink.crossproduct.domain.alert.AlertPriorityType;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.alert.AlertStatsData;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditSearchRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserActivity;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.io.IOBatchesMessageTypes;
import com.vocalink.crossproduct.domain.io.IODashboard;
import com.vocalink.crossproduct.domain.io.IOData;
import com.vocalink.crossproduct.domain.io.IODataAmountDetails;
import com.vocalink.crossproduct.domain.io.IODataDetails;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.IOTransactionsMessageTypes;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.Payment;
import com.vocalink.crossproduct.domain.reference.EnquiryType;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeValidation;
import com.vocalink.crossproduct.domain.reference.ReasonCodeValidation.ReasonCode;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionStatus;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.ScheduleDayDetails;
import com.vocalink.crossproduct.domain.settlement.SettlementCycleSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementDetails;
import com.vocalink.crossproduct.domain.settlement.SettlementDetailsSearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementStatus;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.account.BPSAccount;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlert;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertPriority;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertReferenceData;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStats;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStatsData;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApproval;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalConfirmationResponse;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalStatus;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchDetailed;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchPart;
import com.vocalink.crossproduct.infrastructure.bps.broadcasts.BPSBroadcast;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSDayCycle;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSPayment;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFile;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODashboard;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOData;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOSummary;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSManagedParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantConfiguration;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSUserDetails;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSEnquiryType;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSReasonCodeReference.BPSReasonCode;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSReasonCodeReference.BPSReasonCodeValidation;
import com.vocalink.crossproduct.infrastructure.bps.report.BPSReport;
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecord;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantSettlement;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSScheduleDayDetails;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementCycleSchedule;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementDetails;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementSchedule;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionDetails;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.infrastructure.jpa.activities.UserActivityJpa;
import com.vocalink.crossproduct.infrastructure.jpa.audit.AuditDetailsJpa;
import com.vocalink.crossproduct.infrastructure.jpa.audit.AuditDetailsView;
import com.vocalink.crossproduct.infrastructure.jpa.audit.UserDetailsView;
import com.vocalink.crossproduct.ui.aspects.OccurringEvent;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

  EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

  @Mappings({
      @Mapping(target = "settlementCycleId", source = "settlementCycle"),
      @Mapping(target = "nrOfTransactions", source = "numberOfTransactions"),
      @Mapping(target = "createdAt", source = "sentDateAndTime"),
  })
  Batch toEntity(BPSBatchDetailed batch);

  File toEntity(BPSFile file);

  @Mappings({
      @Mapping(target = "validationLevel", source = "validationLevel", qualifiedByName = "convertBpsEnquiryType"),
  })
  ReasonCodeValidation toEntity(BPSReasonCodeValidation reasonCodeValidation);

  @Named("convertBpsEnquiryType")
  default EnquiryType convertBpsEnquiryType(BPSEnquiryType bpsEnquiryType) {
    return EnquiryType.valueOf(bpsEnquiryType.name());
  }

  ReasonCode toEntity(BPSReasonCode reasonCode);

  AlertReferenceData toEntity(BPSAlertReferenceData alertReferenceData);

  IOData toEntity(BPSIOData ioData);

  Broadcast toEntity(BPSBroadcast broadcast);

  @Mappings({
      @Mapping(target = "id", source = "cycleId"),
      @Mapping(target = "cutOffTime", source = "fileSubmissionCutOffTime")
  })
  Cycle toEntity(BPSCycle cycle);

  DayCycle toEntity(BPSDayCycle cycle);

  IntraDayPositionGross toEntity(BPSIntraDayPositionGross intraDayPositionGross);

  @Mappings({
      @Mapping(target = "files.output", expression = "java(bPSIODataDetails.getAccepted() + bPSIODataDetails.getSubmitted())"),
      @Mapping(target = "files.rejected", expression = "java(Double.valueOf(bPSIODataDetails.getRejected().replace(\"%\", \"\")))"),
      @Mapping(target = "batches", source = "batches", qualifiedByName = "mapBatches"),
      @Mapping(target = "transactions", source = "transactions", qualifiedByName = "mapTransactions")
  })
  IODetails toEntity(BPSIODetails ioDetails);

  @Named("mapBatches")
  default List<IOBatchesMessageTypes> mapBatches(List<BPSIOSummary> batches) {
    return batches.stream().map(e ->
        new IOBatchesMessageTypes(
            getNameByType(e.getMessageType()),
            e.getMessageType(),
            new IODataDetails(
                e.getSubmitted(),
                e.getAccepted(),
                e.getOutput(),
                Double.valueOf(e.getRejected().replaceAll("%", "")))))
        .collect(toList());
  }

  @Named("mapTransactions")
  default List<IOTransactionsMessageTypes> mapTransactions(List<BPSIOSummary> transactions) {
    return transactions.stream().map(e ->
        new IOTransactionsMessageTypes(
            getNameByType(e.getMessageType()),
            e.getMessageType(),
            new IODataAmountDetails(
                e.getSubmitted(),
                e.getAccepted(),
                e.getOutput(),
                Double.valueOf(e.getRejected().replaceAll("%", "")),
                e.getAmountAccepted().getAmount(),
                e.getAmountOutput().getAmount()
            )))
        .collect(toList());
  }

  IODashboard toEntity(BPSIODashboard ioDashboard);

  ParticipantIOData toEntity(BPSParticipantIOData participantIOData);

  MessageDirectionReference toEntity(BPSMessageDirectionReference messageDirectionReference);

  @Mappings({
      @Mapping(target = "status", source = "status", qualifiedByName = "toStatus"),
      @Mapping(target = "statusDetail", source = "statusDetail", qualifiedByName = "toInstructionStatus")
  })
  SettlementDetails toEntity(BPSSettlementDetails settlementDetails);

  @Mappings(
      @Mapping(target = "status", source = "status", qualifiedByName = "toStatus")
  )
  ParticipantSettlement toEntity(BPSParticipantSettlement settlement);

  @Named("toStatus")
  default SettlementStatus convertStatusType(String status) {
    return SettlementStatus.valueOf(status.toUpperCase().replaceAll("[_+-]", "_"));
  }

  default List<ParticipantPosition> toEntity(BPSSettlementPositionWrapper positionsWrapper) {
    return positionsWrapper.getMlSettlementPositions().stream().map(this::toEntity).collect(toList());
  }

  ParticipantPosition toEntity(BPSSettlementPosition position);

  @Named("toInstructionStatus")
  default InstructionStatus convertInstructionStatusType(String status) {
    return InstructionStatus.valueOf(status.toUpperCase().replaceAll("[_+-]", "_"));
  }

  @Mappings({
      @Mapping(source = "sort", target = "sort", qualifiedByName = "setDefaultDateSort")
  })
  BatchEnquirySearchCriteria toEntity(BatchEnquirySearchRequest request);

  @Mappings({
      @Mapping(source = "sort", target = "sort", qualifiedByName = "setDefaultDateSort")
  })
  FileEnquirySearchCriteria toEntity(FileEnquirySearchRequest request);

  AlertSearchCriteria toEntity(AlertSearchRequest request);

  @Mappings({
      @Mapping(source = "cycleId", target = "cycleId"),
      @Mapping(source = "participantId", target = "participantId")
  })
  SettlementDetailsSearchCriteria toEntity(ParticipantSettlementRequest request, String cycleId,
      String participantId);

  SettlementEnquirySearchCriteria toEntity(SettlementEnquiryRequest request);

  SettlementSchedule toEntity(BPSSettlementSchedule cycleSchedule);

  ScheduleDayDetails toEntity(BPSScheduleDayDetails weekDaySchedule);

  @Mappings({
      @Mapping(target = "cycleName", source = "sessionCode"),
      @Mapping(target = "cutOffTime", source = "endTime"),
      @Mapping(target = "settlementStartTime", source = "settlementTime")
  })
  SettlementCycleSchedule toEntity(BPSSettlementCycleSchedule cycleSchedule);

  @Mappings({
      @Mapping(source = "sort", target = "sort", qualifiedByName = "setDefaultDateSort")
  })
  TransactionEnquirySearchCriteria toEntity(TransactionEnquirySearchRequest request);

  @Mappings({
      @Mapping(target = "createdAt", source = "createdDateTime")
  })
  Transaction toEntity(BPSTransaction transaction);

  @Mappings({
      @Mapping(target = "instructionId", source = "txnsInstructionId"),
      @Mapping(target = "status", source = "transactionStatus"),
      @Mapping(target = "createdAt", source = "sentDateTime"),
      @Mapping(target = "amount", source = "transactionAmount"),
      @Mapping(target = "settlementCycleId", source = "settlementCycle")
  })
  Transaction toEntity(BPSTransactionDetails transaction);

  AlertPriorityData toEntity(BPSAlertPriority priorityData);

  Payment toEntity(BPSPayment payment);

  Amount toEntity(BPSAmount amount);

  Account toEntity(BPSAccount priorityData);

  @Mappings({
      @Mapping(target = "entityName", source = "participant.name"),
      @Mapping(target = "entityBic", source = "account.partyCode"),
      @Mapping(target = "iban", source = "account.iban")
  })
  EnquirySenderDetails toEntity(Account account, Participant participant);

  ManagedParticipantsSearchCriteria toEntity(ManagedParticipantsSearchRequest request);

  @Mappings({
      @Mapping(target = "priority", source = "priority", qualifiedByName = "convertAlertPriorityType"),
  })
  Alert toEntity(BPSAlert alert);

  @Mappings({
      @Mapping(target = "priority", source = "priority", qualifiedByName = "convertAlertPriorityType"),
  })
  AlertStatsData toEntity(BPSAlertStatsData alertStatsData);

  @Named("convertAlertPriorityType")
  default AlertPriorityType convertAlertPriorityType(String alertPriorityType) {
    return AlertPriorityType.valueOf(alertPriorityType.toUpperCase());
  }

  AlertStats toEntity(BPSAlertStats alertStats);

  @Mappings({
      @Mapping(target = "id", source = "schemeParticipantIdentifier"),
      @Mapping(target = "bic", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "fundingBic", source = "connectingParty"),
      @Mapping(target = "suspendedTime", source = "effectiveTillDate"),
      @Mapping(target = "organizationId", source = "partyExternalIdentifier"),
      @Mapping(target = "participantType", source = "participantType", qualifiedByName = "convertParticipantType"),
      @Mapping(target = "status", source = "status", qualifiedByName = "convertParticipantStatus")
  })
  Participant toEntity(BPSParticipant bpsParticipant);

  @Mappings({
      @Mapping(target = "id", source = "schemeParticipantIdentifier"),
      @Mapping(target = "bic", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "fundingBic", source = "connectingParty"),
      @Mapping(target = "suspendedTime", source = "effectiveTillDate"),
      @Mapping(target = "organizationId", source = "partyExternalIdentifier"),
      @Mapping(target = "participantType", source = "participantType", qualifiedByName = "convertParticipantType"),
      @Mapping(target = "status", source = "status", qualifiedByName = "convertParticipantStatus")
  })
  Participant toEntity(BPSManagedParticipant bpsParticipant);

  @Named("convertParticipantType")
  default ParticipantType convertParticipantType(String participantType) {
    return ParticipantType.valueOf(participantType.replaceAll("[_+-]", "_"));
  }

  @Named("convertParticipantStatus")
  default ParticipantStatus convertParticipantStatus(String participantStatus) {
    return ParticipantStatus.valueOf(participantStatus);
  }

  BroadcastsSearchCriteria toEntity(BroadcastsSearchParameters parameters);

  ApprovalSearchCriteria toEntity(ApprovalSearchRequest approvalSearchRequest);

  @Mappings({
      @Mapping(target = "status", source = "status", qualifiedByName = "convertApprovalStatus"),
      @Mapping(target = "requestType", source = "requestType", qualifiedByName = "convertBpsApprovalRequestType")
  })
  Approval toEntity(BPSApproval approval);

  ApprovalConfirmationResponse toEntity(BPSApprovalConfirmationResponse approvalConfirmationResponse);

  @Named("convertApprovalStatus")
  default ApprovalStatus convertApprovalStatus(BPSApprovalStatus bpsApprovalStatus) {
    return ApprovalStatus.valueOf(bpsApprovalStatus.name());
  }

  @Named("convertBpsApprovalRequestType")
  default ApprovalRequestType convertBpsApprovalRequestType(BPSApprovalRequestType bpsApprovalRequestType) {
    return ApprovalRequestType.valueOf(bpsApprovalRequestType.name());
  }

  ParticipantConfiguration toEntity(BPSParticipantConfiguration configuration);

  @Mappings({
      @Mapping(target = "participantId", source = "schemeParticipantIdentifier")
  })
  UserDetails toEntity(BPSUserDetails approvalUser);

  RoutingRecord toEntity(BPSRoutingRecord routingRecord);

  @Mappings({
      @Mapping(target = "bic", source = "bic")
  })
  RoutingRecordCriteria toEntity(RoutingRecordRequest request, String bic);

  @Mappings({
      @Mapping(target = "requestType", source = "requestType", qualifiedByName = "convertApprovalRequestType")
  })
  ApprovalChangeCriteria toEntity(ApprovalChangeRequest request);

  @Named("convertApprovalRequestType")
  default ApprovalRequestType convertApprovalRequestType(String approvalRequestType) {
    return ApprovalRequestType.valueOf(approvalRequestType);
  }

  @Mappings({
      @Mapping(target = "approvalId", source = "id"),
  })
  ApprovalConfirmation toEntity(ApprovalConfirmationRequest request, String id);

  ReportSearchCriteria toEntity(ReportsSearchRequest parameters);

  Report toEntity(BPSReport bpsReportBPSPage);

  default <T> Page<T> toEntity(BPSPage<?> page, Class<T> targetType) {
    List<?> sourceItems = page.getItems();

    if (sourceItems == null || sourceItems.isEmpty()) {
      return new Page<>(0, emptyList());
    }

    final Class<?> sourceType = sourceItems.get(0).getClass();

    Method method = stream(this.getClass().getDeclaredMethods())
        .filter(m -> m.getParameterTypes().length == 1)
        .filter(m -> m.getParameterTypes()[0].isAssignableFrom(sourceType))
        .filter(m -> m.getReturnType().isAssignableFrom(targetType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Method by signature not found!"));

    final List<T> targetItems = sourceItems.stream()
        .map(sourceType::cast)
        .map(obj -> {
          try {
            return method.invoke(this, obj);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InfrastructureException(e.getCause(), "DataTransferObject mapping exception!");
          }
        })
        .map(targetType::cast)
        .collect(toList());

    return new Page<>(page.getTotalResults(), targetItems);
  }

  @Mappings({
   @Mapping(target = "batchId", source = "messageIdentifier"),
   @Mapping(target = "createdAt", source = "createdDateTime"),
   @Mapping(target = "senderBic", source = "originator")
  })
  Batch toEntity(BPSBatchPart partialBatch);

  default <T> Page<T> toEntity(BPSResult<?> result, Class<T> targetType) {
    final List<?> sourceData = result.getData();

    if (Objects.isNull(sourceData) || sourceData.isEmpty()) {
      return new Page<>(0, emptyList());
    }

    final Class<?> sourceType = sourceData.get(0).getClass();

    Method method = stream(this.getClass().getDeclaredMethods())
        .filter(m -> m.getParameterTypes().length == 1)
        .filter(m -> m.getParameterTypes()[0].isAssignableFrom(sourceType))
        .filter(m -> m.getReturnType().isAssignableFrom(targetType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Method by signature not found!"));

    final List<T> targetItems = sourceData.stream()
        .map(sourceType::cast)
        .map(obj -> {
          try {
            return method.invoke(this, obj);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InfrastructureException(e.getCause(), "DataTransferObject mapping exception!");
          }
        })
        .map(targetType::cast)
        .collect(toList());

    return new Page<>(result.getSummary().getTotalCount(), targetItems);
  }

  @Mappings({
      @Mapping(target = "activityId", source = "activityId.id")
  })
  AuditDetails toEntity(AuditDetailsJpa details);

  UserActivity toEntity(UserActivityJpa activityJpa);

  AuditSearchRequest toEntity(AuditRequestParams parameters);

  Event toEntity(OccurringEvent occurringEvent);

  @Mappings({
      @Mapping(target = "userId", source = "username")
  })
  UserDetails toEntity(AuditDetails auditDetails);

  @Named("setDefaultDateSort")
  default List<String> setDefaultDateSort(List<String> sort) {
    if (sort == null || sort.isEmpty()) {
      return singletonList("-createdAt");
    }
    return sort;
  }

  @Mappings({
      @Mapping(target = "responseContent", expression = "java(auditDetailsView.getResponseContent())")
  })
  AuditDetails toEntity(AuditDetailsView auditDetailsView);

  AuditDetails toEntity(UserDetailsView userDetailsView);
}
