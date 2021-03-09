package com.vocalink.crossproduct.ui.presenter;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.Result;
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
import org.springframework.stereotype.Component;

@Component
public class SystemPresenter implements Presenter {

  @Override
  public SettlementDashboardDto presentAllParticipantsSettlement(List<Cycle> cycles,
      List<Participant> participants) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public SettlementDashboardDto presentFundingParticipantSettlement(List<Cycle> cycles,
      List<Participant> participants, Participant fundingParticipant,
      List<IntraDayPositionGross> intraDays) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto presentParticipantSettlementDetails(
      List<Cycle> cycles, List<ParticipantPosition> positions, Participant participant) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ParticipantDashboardSettlementDetailsDto presentFundedParticipantSettlementDetails(
      List<Cycle> cycles, List<ParticipantPosition> positions, Participant participant,
      Participant fundingParticipant, IntraDayPositionGross intradayPositionGross) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ClientType getClientType() {
    return ClientType.SYSTEM;
  }

  @Override
  public IODashboardDto presentInputOutput(List<Participant> participants,
      List<ParticipantIOData> ioData, LocalDate date) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public IODetailsDto presentIoDetails(Participant participant, IODetails ioDetails,
      LocalDate date) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public AlertReferenceDataDto presentAlertReference(AlertReferenceData alertsReference) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public AlertStatsDto presentAlertStats(AlertStats alertStats) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public List<FileStatusesTypeDto> presentFileReferencesFor(List<FileReference> fileReferences,
      String enquiryType) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<AlertDto> presentAlert(Page<Alert> alerts) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public List<MessageDirectionReferenceDto> presentMessageDirectionReferences(
      List<MessageDirectionReference> messageDirectionReferences) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<FileDto> presentFiles(Integer totalResults, List<File> items) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public List<DayCycleDto> presentCycleDateReferences(List<DayCycle> dayCycles) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public List<ParticipantReferenceDto> presentParticipantReferences(
      List<ParticipantReference> participants) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public FileDetailsDto presentFileDetails(File file, Participant participant,
      Account account) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<BatchDto> presentBatches(Integer totalResults, List<Batch> items) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<TransactionDto> presentTransactions(Integer totalResults, List<Transaction> items) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public BatchDetailsDto presentBatchDetails(Batch batch, Participant participant,
      File file) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(ParticipantSettlement settlement,
      List<Participant> participants, Participant participant) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ParticipantSettlementDetailsDto presentSettlementDetails(ParticipantSettlement settlement,
      List<Participant> participants, Participant participant, Participant settlementBank) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<ParticipantSettlementCycleDto> presentSettlements(
      Page<ParticipantSettlement> settlements, List<Participant> participants) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public LatestSettlementCyclesDto presentLatestCycles(
      List<Cycle> cycles) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public SettlementScheduleDto presentSchedule(SettlementSchedule schedule) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public TransactionDetailsDto presentTransactionDetails(Transaction transaction,
      EnquirySenderDetails sender) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public TransactionDetailsDto presentTransactionDetails(Transaction transaction,
      EnquirySenderDetails sender, EnquirySenderDetails receiver) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ApprovalDetailsDto presentApprovalDetails(Approval approval) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<ManagedParticipantDto> presentManagedParticipants(
      Page<Participant> participants) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<BroadcastDto> presentBroadcasts(int totalResults,
      List<BroadcastDto> broadcastDtos) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public BroadcastDto presentBroadcast(Broadcast broadcastDto,
      List<Participant> references) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<ApprovalDetailsDto> presentApproval(Page<Approval> approvals) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<RoutingRecordDto> presentRoutingRecords(Page<RoutingRecord> routingRecords) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Participant fundingParticipant, Account account) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ManagedParticipantDetailsDto presentManagedParticipantDetails(Participant participant,
      ParticipantConfiguration configuration, Account account) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public PageDto<ReportDto> presentReports(Page<Report> reports) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public Resource presentStream(InputStream file) {
    throw new RuntimeException("System API not implemented yet");
  }

  @Override
  public ApprovalConfirmationResponseDto presentApprovalResponse(
      ApprovalConfirmationResponse response) {
    throw new RuntimeException("System API not implemented yet");
  }
}
