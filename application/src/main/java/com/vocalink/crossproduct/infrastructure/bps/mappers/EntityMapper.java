package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static com.vocalink.crossproduct.domain.reference.MessageReferenceDirection.RECEIVING;
import static com.vocalink.crossproduct.domain.reference.MessageReferenceDirection.SENDING;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.MapperUtils.getNameByType;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Amount;
import com.vocalink.crossproduct.domain.Page;
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
import com.vocalink.crossproduct.domain.approval.ApprovalCreationResponse;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.AuditSearchRequest;
import com.vocalink.crossproduct.domain.audit.Event;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
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
import com.vocalink.crossproduct.domain.participant.SuspensionLevel;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.Payment;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection;
import com.vocalink.crossproduct.domain.reference.MessageReferenceLevel;
import com.vocalink.crossproduct.domain.reference.MessageReferenceType;
import com.vocalink.crossproduct.domain.reference.OutputFlowReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference;
import com.vocalink.crossproduct.domain.reference.ReasonCodeReference.ReasonCode;
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
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlert;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertPriority;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertReferenceData;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStats;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStatsData;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApproval;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalConfirmationResponse;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalCreationResponse;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalStatus;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchDetailed;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchPart;
import com.vocalink.crossproduct.infrastructure.bps.broadcasts.BPSBroadcast;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycleStatus;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSDayCycle;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSPayment;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFile;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOBatchSummary;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODashboard;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOData;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOTransactionSummary;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSDebitCapThreshold;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSManagedParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantConfiguration;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSUserDetails;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSOutputFlowReference;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSReasonCodeReference;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSReasonCodeReference.BPSReasonCode;
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
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Context;
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
      @Mapping(target = "senderBank", source = "instructingAgentName"),
      @Mapping(target = "senderBic", source = "instructingAgent"),
      @Mapping(target = "receiverBank", source = "instructedAgentName"),
      @Mapping(target = "receiverBic", source = "instructedAgent"),
      @Mapping(target = "messageDirection", source = "messageDirection", qualifiedByName = "convertToMessageDirection")
  })
  Batch toEntity(BPSBatchDetailed batch);

  @Mappings({
      @Mapping(target = "messageDirection", source = "messageDirection", qualifiedByName = "convertToMessageDirection")
  })
  File toEntity(BPSFile file);

  ReasonCode toEntity(BPSReasonCode reasonCode);

  AlertReferenceData toEntity(BPSAlertReferenceData alertReferenceData);

  IOData toEntity(BPSIOData ioData);

  Broadcast toEntity(BPSBroadcast broadcast);

  @Mappings({
      @Mapping(target = "id", source = "cycleId"),
      @Mapping(target = "status", source = "status", qualifiedByName = "toCycleStatus"),
      @Mapping(target = "cutOffTime", source = "fileSubmissionCutOffTime")
  })
  Cycle toEntity(BPSCycle cycle);

  @Mappings({
      @Mapping(target = "id", source = "cycleId"),
      @Mapping(target = "cutOffTime", source = "fileSubmissionCutOffTime"),
      @Mapping(target = "status", source = "status", qualifiedByName = "toCycleStatus"),
      @Mapping(target = "totalPositions", source = "cycleId", qualifiedByName = "setTotalPositions")
  })
  Cycle toEntity(BPSCycle cycle, @Context List<BPSSettlementPosition> positions);

  @Named("setTotalPositions")
  default List<ParticipantPosition> setTotalPositions(String cycleId,
      @Context List<BPSSettlementPosition> positions) {
    return positions.stream()
        .filter(pos -> pos.getCycleId().equals(cycleId))
        .map(this::toEntity)
        .collect(toList());
  }

  DayCycle toEntity(BPSDayCycle cycle);

  IntraDayPositionGross toEntity(BPSIntraDayPositionGross intraDayPositionGross);

  @Mappings({
      @Mapping(target = "files.rejected", expression = "java(Double.valueOf(bPSIOFileSummary.getRejected().replace(\"%\", \"\")))"),
      @Mapping(target = "batches", source = "batches", qualifiedByName = "mapBatches"),
      @Mapping(target = "transactions", source = "transactions", qualifiedByName = "mapTransactions")
  })
  IODetails toEntity(BPSIODetails ioDetails);

  @Named("mapBatches")
  default List<IOBatchesMessageTypes> mapBatches(List<BPSIOBatchSummary> batches) {
    if (batches == null) {
      return Collections.emptyList();
    }
    return batches.stream().map(e ->
        new IOBatchesMessageTypes(
            getNameByType(e.getMessageType()),
            e.getMessageType(),
            new IODataDetails(
                e.getSubmitted(),
                e.getAccepted(),
                Double.valueOf(e.getRejected().replace("%", "")),
                e.getOutput())))
        .collect(toList());
  }

  @Named("mapTransactions")
  default List<IOTransactionsMessageTypes> mapTransactions(
      List<BPSIOTransactionSummary> transactions) {
    if (transactions == null) {
      return Collections.emptyList();
    }
    return transactions.stream().map(e ->
        new IOTransactionsMessageTypes(
            getNameByType(e.getMessageType()),
            e.getMessageType(),
            new IODataAmountDetails(
                e.getSubmitted(),
                e.getAccepted(),
                e.getOutput(),
                Double.valueOf(e.getRejected().replace("%", "")),
                e.getAmountAccepted().getAmount(),
                e.getAmountOutput().getAmount()
            )))
        .collect(toList());
  }

  IODashboard toEntity(BPSIODashboard ioDashboard);

  ParticipantIOData toEntity(BPSParticipantIOData participantIOData);

  @Mappings({
      @Mapping(target = "direction", source = "messageDirection", qualifiedByName = "toMessageDirectionList"),
      @Mapping(target = "level", source = "level", qualifiedByName = "toMessageLevel"),
      @Mapping(target = "subType", source = "type", qualifiedByName = "toMessageType")

  })
  MessageDirectionReference toEntity(BPSMessageDirectionReference messageDirectionReference);

  @Named("convertToMessageDirection")
  default MessageReferenceDirection convertToMessageDirection(String messageDirection) {
    if (nonNull(messageDirection) && messageDirection.equalsIgnoreCase("input")) {
      return SENDING;
    }
    if (nonNull(messageDirection) && messageDirection.equalsIgnoreCase("output")) {
      return RECEIVING;
    }
    return null;
  }

  @Named("toMessageDirectionList")
  default List<MessageReferenceDirection> toMessageDirectionList(List<String> messageDirections) {
    return messageDirections.stream()
        .map(this::convertToMessageDirection)
        .collect(toList());
  }

  @Named("toMessageLevel")
  default List<MessageReferenceLevel> toMessageLevel(List<String> levels) {
    return levels.stream()
        .map(level -> MessageReferenceLevel.valueOf(level.toUpperCase()))
        .collect(toList());
  }

  @Named("toMessageType")
  default List<MessageReferenceType> toMessageType(List<String> types) {
    return types.stream()
        .map(type -> MessageReferenceType.valueOf(type.toUpperCase().replaceAll("[ _+-]", "_")))
        .collect(toList());
  }

  @Mappings({
      @Mapping(target = "status", source = "status", qualifiedByName = "toCycleStatus"),
      @Mapping(target = "statusDetail", source = "statusDetail", qualifiedByName = "toInstructionStatus")
  })
  SettlementDetails toEntity(BPSSettlementDetails settlementDetails);

  @Mappings(
      @Mapping(target = "status", source = "status", qualifiedByName = "toCycleStatus")
  )
  ParticipantSettlement toEntity(BPSParticipantSettlement settlement);

  @Named("toCycleStatus")
  default CycleStatus toCycleStatus(BPSCycleStatus bpsCycleStatus) {
    return CycleStatus.valueOf(bpsCycleStatus.name());
  }

  default List<ParticipantPosition> toEntity(BPSSettlementPositionWrapper positionsWrapper) {
    return positionsWrapper.getMlSettlementPositions().stream().map(this::toEntity)
        .collect(toList());
  }

  ParticipantPosition toEntity(BPSSettlementPosition position);

  @Named("toInstructionStatus")
  default InstructionStatus convertInstructionStatusType(String status) {
    if (status == null) {
      return null;
    }
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

  SettlementDetailsSearchCriteria toEntity(ParticipantSettlementRequest request);

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
      @Mapping(target = "offset", source = "request.offset"),
      @Mapping(target = "limit", source = "request.limit"),
      @Mapping(target = "sort", source = "request.sort", qualifiedByName = "setDefaultDateSort"),
      @Mapping(target = "dateFrom", source = "request.dateFrom"),
      @Mapping(target = "dateTo", source = "request.dateTo"),
      @Mapping(target = "cycleId", source = "request.cycleId"),
      @Mapping(target = "messageType", source = "request.messageType"),
      @Mapping(target = "sendingParticipant", source = "request.sendingParticipant"),
      @Mapping(target = "receivingParticipant", source = "request.receivingParticipant"),
      @Mapping(target = "debtor", source = "request.debtor"),
      @Mapping(target = "creditor", source = "request.creditor"),
      @Mapping(target = "status", source = "request.status"),
      @Mapping(target = "reasonCode", source = "request.reasonCode"),
      @Mapping(target = "id", source = "request.id"),
      @Mapping(target = "sendingAccount", source = "request.sendingAccount"),
      @Mapping(target = "receivingAccount", source = "request.receivingAccount"),
      @Mapping(target = "txnFrom", source = "request.txnFrom"),
      @Mapping(target = "txnTo", source = "request.txnTo"),
      @Mapping(target = "valueDate", source = "valueDate")
  })
  TransactionEnquirySearchCriteria toEntity(TransactionEnquirySearchRequest request,
      ZonedDateTime valueDate);

  @Mappings({
      @Mapping(target = "createdAt", source = "createdDateTime"),
      @Mapping(target = "creditorBic", source = "creditor"),
      @Mapping(target = "debtorBic", source = "debtor")
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
      @Mapping(target = "status", source = "status", qualifiedByName = "convertParticipantStatus"),
      @Mapping(target = "suspensionLevel", source = "suspensionLevel", qualifiedByName = "toSuspensionLevelType"),
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
      @Mapping(target = "status", source = "status", qualifiedByName = "convertParticipantStatus"),
      @Mapping(target = "suspensionLevel", source = "suspensionLevel", qualifiedByName = "toSuspensionLevelType")
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

  @Named("toSuspensionLevelType")
  default SuspensionLevel toSuspensionLevelType(String suspensionLevel) {
    if (suspensionLevel == null) {
      return null;
    }
    return SuspensionLevel.valueOf(suspensionLevel.replaceAll("[+-]", "_"));
  }

  BroadcastsSearchCriteria toEntity(BroadcastsSearchParameters parameters);

  ApprovalSearchCriteria toEntity(ApprovalSearchRequest approvalSearchRequest);

  @Mappings({
      @Mapping(target = "status", source = "status", qualifiedByName = "convertApprovalStatus"),
      @Mapping(target = "requestType", source = "requestType", qualifiedByName = "convertBpsApprovalRequestType")
  })
  Approval toEntity(BPSApproval approval);

  ApprovalConfirmationResponse toEntity(
      BPSApprovalConfirmationResponse approvalConfirmationResponse);

  @Named("convertApprovalStatus")
  default ApprovalStatus convertApprovalStatus(BPSApprovalStatus bpsApprovalStatus) {
    if (bpsApprovalStatus != null) {
      return ApprovalStatus.valueOf(bpsApprovalStatus.name());
    }
    return null;
  }

  @Named("convertBpsApprovalRequestType")
  default ApprovalRequestType convertBpsApprovalRequestType(
      BPSApprovalRequestType bpsApprovalRequestType) {
    if (bpsApprovalRequestType != null) {
      return ApprovalRequestType.valueOf(bpsApprovalRequestType.name());
    }
    return null;
  }

  ApprovalCreationResponse toEntity(BPSApprovalCreationResponse bpsApprovalResponse);

  @Mappings({
      @Mapping(target = "participantType", source = "participantType", qualifiedByName = "convertParticipantType"),
      @Mapping(target = "debitCapLimit", source = "debitCapThreshold", qualifiedByName = "getDebitCapLimit"),
      @Mapping(target = "debitCapLimitThresholds", source = "debitCapThreshold", qualifiedByName = "getDebitCapLimitThresholds")
  })
  ParticipantConfiguration toEntity(BPSParticipantConfiguration configuration);

  @Named("getDebitCapLimit")
  default BigDecimal getDebitCapLimit(List<BPSDebitCapThreshold> debitCapThreshold) {
    if (debitCapThreshold == null) {
      return null;
    }
    return debitCapThreshold.stream().findFirst()
        .map(BPSDebitCapThreshold::getLimitThresholdAmounts)
        .map(BPSAmount::getAmount)
        .orElse(null);
  }

  @Named("getDebitCapLimitThresholds")
  default List<Double> getDebitCapLimitThresholds(List<BPSDebitCapThreshold> debitCapThreshold) {
    if (debitCapThreshold == null) {
      return null;
    }
    return debitCapThreshold.stream()
        .map(BPSDebitCapThreshold::getWarningThresholdPercentage)
        .collect(toList());
  }

  @Mappings({
      @Mapping(target = "participantId", source = "schemeParticipantIdentifier")
  })
  UserDetails toEntity(BPSUserDetails approvalUser);

  RoutingRecord toEntity(BPSRoutingRecord routingRecord);

  @Mappings({
      @Mapping(target = "participantId", source = "participantId")
  })
  RoutingRecordCriteria toEntity(RoutingRecordRequest request, String participantId);

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

  @Mappings({
      @Mapping(target = "participants", source = "participants")
  })
  ReportSearchCriteria toEntity(ReportsSearchRequest parameters, List<Participant> participants);

  @Mappings({
      @Mapping(target = "createdAt", source = "reportDate"),
      @Mapping(target = "participantIdentifier", source = "partyCode")
  })
  Report toEntity(BPSReport bpsReport);

  OutputFlowReference toEntity(BPSOutputFlowReference bpsReport);

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
            throw new InfrastructureException(e.getCause(),
                "DataTransferObject mapping exception!");
          }
        })
        .map(targetType::cast)
        .collect(toList());

    return new Page<>(page.getTotalResults(), targetItems);
  }

  @Mappings({
      @Mapping(target = "batchId", source = "messageIdentifier"),
      @Mapping(target = "createdAt", source = "createdDateTime"),
      @Mapping(target = "senderBic", source = "instructingAgent"),
      @Mapping(target = "receiverBic", source = "instructedAgent")

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
            throw new InfrastructureException(e.getCause(),
                "DataTransferObject mapping exception!");
          }
        })
        .map(targetType::cast)
        .collect(toList());

    return new Page<>(result.getSummary() == null ? sourceData.size(): result.getSummary().getTotalCount(), targetItems);
  }

  AuditDetails toEntity(AuditDetailsJpa details);

  AuditSearchRequest toEntity(AuditRequestParams parameters);

  Event toEntity(OccurringEvent occurringEvent);

  @Mappings({
      @Mapping(target = "userId", source = "username")
  })
  UserDetails toEntity(AuditDetails auditDetails);

  @Mappings({
      @Mapping(target = "userId", source = "username")
  })
  UserDetails toUserEntity(AuditDetailsJpa details);

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

  ReasonCodeReference toEntity(BPSReasonCodeReference bpsReasonCodeReference);
}
