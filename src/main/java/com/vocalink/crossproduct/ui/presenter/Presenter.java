package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.transaction.Transaction;
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
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.io.Resource;

public interface Presenter {

  SettlementDashboardDto presentAllParticipantsSettlement(
      List<Cycle> cycles,
      List<Participant> participants);

  SettlementDashboardDto presentFundingParticipantSettlement(
      List<Cycle> cycles,
      List<Participant> participants,
      Participant fundingParticipant,
      List<IntraDayPositionGross> intraDays);

  ParticipantDashboardSettlementDetailsDto presentParticipantSettlementDetails(List<Cycle> cycles,
      List<ParticipantPosition> positionsDetails,
      Participant participant);

  ParticipantDashboardSettlementDetailsDto presentFundedParticipantSettlementDetails(
      List<Cycle> cycles,
      List<ParticipantPosition> positionsDetails,
      Participant participant,
      Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross);

  ClientType getClientType();

  IODashboardDto presentInputOutput(List<Participant> participants, List<ParticipantIOData> ioData,
      LocalDate date);

  IODetailsDto presentIoDetails(Participant participant, IODetails ioDetails,
      LocalDate date);

  AlertReferenceDataDto presentAlertReference(AlertReferenceData alertsReference);

  AlertStatsDto presentAlertStats(AlertStats alertStats);

  PageDto<AlertDto> presentAlert(Page<Alert> alerts);

  List<ParticipantReferenceDto> presentParticipantReferences(
      List<ParticipantReference> participants);

  List<FileStatusesTypeDto> presentFileReferencesFor(List<FileReference> fileReferences,
      String enquiryType);

  List<MessageDirectionReferenceDto> presentMessageDirectionReferences(
      List<MessageDirectionReference> messageDirectionReferences);

  PageDto<FileDto> presentFiles(Integer totalResults, List<File> items);

  List<DayCycleDto> presentCycleDateReferences(List<DayCycle> dayCycles);

  FileDetailsDto presentFileDetails(File file, Participant participant,
      Account account);

  PageDto<BatchDto> presentBatches(Page<Batch> batches);

  PageDto<TransactionDto> presentTransactions(Page<Transaction> transactions);

  BatchDetailsDto presentBatchDetails(Batch batch);

  ParticipantSettlementDetailsDto presentSettlementDetails(ParticipantSettlement settlement,
      List<Participant> participants);

  PageDto<ParticipantSettlementCycleDto> presentSettlements(Page<ParticipantSettlement> settlements,
      List<Participant> participants);

  LatestSettlementCyclesDto presentLatestCycles(List<Cycle> cycles);

  SettlementScheduleDto presentSchedule(SettlementSchedule schedule);

  TransactionDetailsDto presentTransactionDetails(Transaction transaction,
      EnquirySenderDetails sender);

  TransactionDetailsDto presentTransactionDetails(Transaction transaction,
      EnquirySenderDetails sender, EnquirySenderDetails receiver);

  ApprovalDetailsDto presentApprovalDetails(Approval approval);

  PageDto<ManagedParticipantDto> presentManagedParticipants(Page<Participant> participants);

  PageDto<BroadcastDto> presentBroadcasts(int totalResults, List<BroadcastDto> broadcastDtos);

  BroadcastDto presentBroadcast(Broadcast broadcastDto, List<Participant> participants);

  PageDto<ApprovalDetailsDto> presentApproval(Page<Approval> approvals);

  PageDto<RoutingRecordDto> presentRoutingRecords(Page<RoutingRecord> routingRecords);

  ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Participant fundingParticipant, Account account);

  ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Account account);

  PageDto<ReportDto> presentReports(Page<Report> reports);

  Resource presentStream(InputStream file);

  ApprovalConfirmationResponseDto presentApprovalResponse(ApprovalConfirmationResponse response);
}
