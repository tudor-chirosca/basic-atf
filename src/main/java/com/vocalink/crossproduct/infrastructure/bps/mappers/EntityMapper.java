package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Amount;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.Result;
import com.vocalink.crossproduct.domain.Result.ResultSummary;
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
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IOBatchesMessageTypes;
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
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionStatus;
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementStatus;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult.BPSResultSummary;
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
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileReference;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOBatchesMessageTypes;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOData;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODataAmountDetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODataDetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOTransactionsMessageTypes;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantConfiguration;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.bps.report.BPSReport;
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecord;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantInstruction;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantSettlement;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementSchedule;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction;
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
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
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
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
      @Mapping(target = "sender", source = "senderBic"),
      @Mapping(target = "nrOfTransactions", source = "numberOfTransactions"),
      @Mapping(target = "createdAt", source = "sentDateAndTime"),
  })
  Batch toEntity(BPSBatchDetailed batch);

  File toEntity(BPSFile file);

  @Named("convertToDate")
  default LocalDate convertToDate(ZonedDateTime date) {
    return date.toLocalDate();
  }

  FileReference toEntity(BPSFileReference reference);

  AlertReferenceData toEntity(BPSAlertReferenceData alertReferenceData);

  IOBatchesMessageTypes toEntity(BPSIOBatchesMessageTypes batchesMessageTypes);

  IOData toEntity(BPSIOData ioData);

  IODataAmountDetails toEntity(BPSIODataAmountDetails ioDataAmountDetails);

  IODataDetails toEntity(BPSIODataDetails ioDataDetails);

  IOTransactionsMessageTypes toEntity(BPSIOTransactionsMessageTypes transactionsMessageTypes);

  Broadcast toEntity(BPSBroadcast broadcast);

  @Mappings({
      @Mapping(target = "id", source = "cycleId"),
      @Mapping(target = "cutOffTime", source = "fileSubmissionCutOffTime")
  })
  Cycle toEntity(BPSCycle cycle);

  DayCycle toEntity(BPSDayCycle cycle);

  IntraDayPositionGross toEntity(BPSIntraDayPositionGross intraDayPositionGross);

  IODetails toEntity(BPSIODetails ioDetails);

  ParticipantIOData toEntity(BPSParticipantIOData participantIOData);

  MessageDirectionReference toEntity(BPSMessageDirectionReference messageDirectionReference);

  @Mappings(
      @Mapping(target = "status", source = "status", qualifiedByName = "toStatus")
  )
  ParticipantSettlement toEntity(BPSParticipantSettlement settlement);

  @Named("toStatus")
  default SettlementStatus convertStatusType(String status) {
    return SettlementStatus.valueOf(status.toUpperCase().replaceAll("[_+-]", "_"));
  }

  @Mappings({
      @Mapping(target = "instructions.totalResults", source = "instructions.totalResults"),
      @Mapping(target = "instructions", source = "instructions"),
      @Mapping(target = "instructions.items", source = "instructions.items", qualifiedByName = "doInstructions"),
      @Mapping(target = "status", source = "settlement.status", qualifiedByName = "toStatus")
  })
  ParticipantSettlement toEntity(BPSParticipantSettlement settlement,
      BPSPage<BPSParticipantInstruction> instructions);

  @Named("doInstructions")
  default List<ParticipantInstruction> doInstructions(List<BPSParticipantInstruction> instructions) {
    return instructions.stream().map(this::toEntity).collect(toList());
  }

  default List<ParticipantPosition> toEntity(BPSSettlementPositionWrapper positionsWrapper) {
    return positionsWrapper.getMlSettlementPositions().stream().map(this::toEntity).collect(toList());
  }

  ParticipantPosition toEntity(BPSSettlementPosition position);

  @Mapping(source = "status", target = "status", qualifiedByName = "toInstructionStatus")
  ParticipantInstruction toEntity(BPSParticipantInstruction instruction);

  @Named("toInstructionStatus")
  default InstructionStatus convertInstructionStatusType(String status) {
    return InstructionStatus.valueOf(status.toUpperCase().replaceAll("[_+-]", "_"));
  }

  BatchEnquirySearchCriteria toEntity(BatchEnquirySearchRequest request);

  FileEnquirySearchCriteria toEntity(FileEnquirySearchRequest request);

  AlertSearchCriteria toEntity(AlertSearchRequest request);

  @Mappings({
      @Mapping(source = "cycleId", target = "cycleId"),
      @Mapping(source = "participantId", target = "participantId")
  })
  InstructionEnquirySearchCriteria toEntity(ParticipantSettlementRequest request, String cycleId,
      String participantId);

  SettlementEnquirySearchCriteria toEntity(SettlementEnquiryRequest request);

  @Mappings({
      @Mapping(target = "weekdayCycles", source = "weekdayCycles"),
      @Mapping(target = "weekendCycles", source = "weekendCycles")
  })
  SettlementSchedule toEntity(BPSSettlementSchedule bpsSettlementSchedule);

  TransactionEnquirySearchCriteria toEntity(TransactionEnquirySearchRequest request);

  @Mappings({
      @Mapping(target = "createdAt", source = "createdDateTime")
  })
  Transaction toEntity(BPSTransaction transaction);

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
      @Mapping(target = "participantIdentifier", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "participantType", source = "participantType", qualifiedByName = "convertParticipantType"),
      @Mapping(target = "connectingParticipantId", source = "connectingParty"),
  })
  ParticipantReference toReference(BPSParticipant bpsParticipant);

  @Mappings({
      @Mapping(target = "id", source = "schemeParticipantIdentifier"),
      @Mapping(target = "bic", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "fundingBic", source = "connectingParty"),
      @Mapping(target = "suspendedTime", source = "effectiveTillDate"),
      @Mapping(target = "participantType", source = "participantType", qualifiedByName = "convertParticipantType"),
      @Mapping(target = "status", source = "status", qualifiedByName = "convertParticipantStatus")
  })
  Participant toEntity(BPSParticipant bpsParticipant);

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
   @Mapping(target = "sender", source = "originator")
  })
  Batch toEntity(BPSBatchPart partialBatch);

  ResultSummary toEntity(BPSResultSummary summary);

  default <T> Result<T> toEntity(BPSResult<?> result, Class<T> targetType) {
    final List<?> sourceData = result.getData();

    if (Objects.isNull(sourceData) || sourceData.isEmpty()) {
      return new Result<>(Collections.emptyList(), ResultSummary.empty());
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

    final ResultSummary resultSummary = toEntity(result.getSummary());

    return new Result<>(targetItems, resultSummary);
  }
}
