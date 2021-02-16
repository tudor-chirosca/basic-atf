package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Amount;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertPriorityData;
import com.vocalink.crossproduct.domain.alert.AlertPriorityType;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.alert.AlertStatsData;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
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
import com.vocalink.crossproduct.infrastructure.bps.account.BPSAccount;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlert;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertPriority;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStats;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStatsData;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApproval;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalStatus;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSPayment;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantConfiguration;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.bps.report.BPSReport;
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecord;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantInstruction;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantSettlement;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementSchedule;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
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
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

  EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

  IODetails toEntity(BPSIODetails input);

  ParticipantIOData toEntity(BPSParticipantIOData input);

   MessageDirectionReference toEntity(BPSMessageDirectionReference alertReferenceData);

  @Mappings(
      @Mapping(target = "status", source = "status", qualifiedByName = "toStatus")
  )
  ParticipantSettlement toEntity(BPSParticipantSettlement settlement);

  @Mappings(
      @Mapping(target = "items", source = "items", qualifiedByName = "toEntity")
  )
  Page<ParticipantSettlement> toEntity(BPSPage<BPSParticipantSettlement> settlements);

  @Named("toEntity")
  default List<ParticipantSettlement> convertStatusType(
      List<BPSParticipantSettlement> settlements) {
    return settlements.stream().map(this::toEntity).collect(toList());
  }

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

  Page<Transaction> toTransactionPageEntity(BPSPage<BPSTransaction> transactions);

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

  Page<Alert> toAlertPageEntity(BPSPage<BPSAlert> alerts);

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

  Page<Participant> toEntityParticipant(BPSPage<BPSParticipant> participants);

  BroadcastsSearchCriteria toEntity(BroadcastsSearchParameters parameters);

  ApprovalSearchCriteria toEntity(ApprovalSearchRequest approvalSearchRequest);

  @Mappings({
      @Mapping(target = "status", source = "status", qualifiedByName = "convertApprovalStatus"),
      @Mapping(target = "requestType", source = "requestType", qualifiedByName = "convertBpsApprovalRequestType")
  })
  Approval toEntity(BPSApproval approvalDetails);

  @Named("convertApprovalStatus")
  default ApprovalStatus convertApprovalStatus(BPSApprovalStatus bpsApprovalStatus) {
    return ApprovalStatus.valueOf(bpsApprovalStatus.name());
  }

  @Named("convertBpsApprovalRequestType")
  default ApprovalRequestType convertBpsApprovalRequestType(BPSApprovalRequestType bpsApprovalRequestType) {
    return ApprovalRequestType.valueOf(bpsApprovalRequestType.name());
  }

  Page<Approval> toApprovalsEntity(BPSPage<BPSApproval> approvalDetailsPage);

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

  ReportSearchCriteria toEntity(ReportsSearchRequest parameters);

  Page<Report> toPagedReportEntity(BPSPage<BPSReport> bpsReportBPSPage);
}
