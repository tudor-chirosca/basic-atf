package com.vocalink.crossproduct.ui.presenter.mapper;

import static com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDING;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.account.Account;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertPriorityData;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.approval.Approval;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.audit.AuditDetails;
import com.vocalink.crossproduct.domain.audit.UserActivity;
import com.vocalink.crossproduct.domain.audit.UserDetails;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.configuration.Configuration;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.cycle.DayCycle;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.io.IODashboard;
import com.vocalink.crossproduct.domain.io.IOData;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.report.Report;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementCycleSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementDetails;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.validation.ValidationApproval;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertPriorityDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.audit.AuditDto;
import com.vocalink.crossproduct.ui.dto.audit.UserActivityDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.cycle.DayCycleDto;
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODataDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.permission.CurrentUserInfoDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementCycleScheduleDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto;
import com.vocalink.crossproduct.ui.exceptions.UILayerException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper MAPPER = Mappers.getMapper(DTOMapper.class);

  @Mappings({
      @Mapping(target = "id", source = "sessionInstanceId"),
  })
  DayCycleDto toDto(DayCycle dayCycle);

  CycleDto toDto(Cycle cycle);

  ApprovalConfirmationResponseDto toDto(ApprovalConfirmationResponse reponse);

  AlertReferenceDataDto toDto(AlertReferenceData alertReferenceData);

  AlertStatsDto toDto(AlertStats alertStats);

  MessageDirectionReferenceDto toDto(MessageDirectionReference alert);

  @Mapping(target = "dateFrom", source = "date")
  IODetailsDto toDto(IODetails ioDetails, Participant participant, LocalDate date);

  @Mappings({
      @Mapping(target = "participantIdentifier", source = "id"),
      @Mapping(target = "connectingParticipantId", source = "fundingBic")
  })
  ParticipantReferenceDto toReferenceDto(Participant participant);

  @Mappings({
      @Mapping(target = "debitCap", source = "debitCapAmount.amount"),
      @Mapping(target = "debitPosition", source = "debitPositionAmount.amount")
  })
  IntraDayPositionGrossDto toDto(IntraDayPositionGross intraDayPositionGross);

  @Mapping(target = "participant", source = "participant")
  @Mapping(target = "currentPosition", source = "currentCycle.totalPositions", qualifiedByName = "generatePosition")
  @Mapping(target = "previousPosition", source = "previousCycle.totalPositions", qualifiedByName = "generatePosition")
  TotalPositionDto toDto(Participant participant, Cycle currentCycle, Cycle previousCycle,
      @Context String participantID);

  @Mapping(target = "participant", source = "participant")
  @Mapping(target = "currentPosition", source = "currentCycle.totalPositions", qualifiedByName = "generatePosition")
  @Mapping(target = "previousPosition", source = "previousCycle.totalPositions", qualifiedByName = "generatePosition")
  @Mapping(target = "intraDayPositionGross", source = "intraDays", qualifiedByName = "generateIntraDays")
  TotalPositionDto toDto(Participant participant, Cycle currentCycle, Cycle previousCycle,
      List<IntraDayPositionGross> intraDays, @Context String participantID);

  @Named("generatePosition")
  default ParticipantPositionDto generatePosition(List<ParticipantPosition> positions,
      @Context String participantId) {
    if (positions == null) {
      return ParticipantPositionDto.builder().build();
    }
    return positions.stream()
        .filter(pos -> pos.getParticipantId().equals(participantId))
        .findFirst()
        .map(MAPPER::toDto)
        .orElse(ParticipantPositionDto.builder().build());
  }

  @Named("generateIntraDays")
  default IntraDayPositionGrossDto generateIntraDays(List<IntraDayPositionGross> intraDays,
      @Context String participantId) {
    if (intraDays == null) {
      return IntraDayPositionGrossDto.builder().build();
    }
    return intraDays.stream()
        .filter(pos -> pos.getDebitParticipantId().equals(participantId))
        .findFirst()
        .map(MAPPER::toDto)
        .orElse(IntraDayPositionGrossDto.builder().build());
  }

  @Mapping(target = "positions", source = "positions")
  @Mapping(target = "currentCycle", source = "currentCycle")
  @Mapping(target = "previousCycle", source = "previousCycle")
  SettlementDashboardDto toDto(Cycle currentCycle, Cycle previousCycle,
      List<TotalPositionDto> positions);

  @Mapping(target = "currentCycle", source = "currentCycle")
  @Mapping(target = "previousCycle", source = "previousCycle")
  @Mapping(target = "positions", source = "positions")
  @Mapping(target = "fundingParticipant", source = "fundingParticipant")
  @Mapping(target = "currentPositionTotals", source = "positions", qualifiedByName = "countCurrentPositionTotals")
  @Mapping(target = "previousPositionTotals", source = "positions", qualifiedByName = "countPreviousPositionTotals")
  @Mapping(target = "intraDayPositionTotals", source = "intraDays", qualifiedByName = "countIntraDayTotals")
  SettlementDashboardDto toDto(Cycle currentCycle, Cycle previousCycle,
      List<TotalPositionDto> positions, ParticipantDto fundingParticipant,
      List<IntraDayPositionGross> intraDays);

  @Named("countCurrentPositionTotals")
  default PositionDetailsTotalsDto countCurrentPositionTotals(List<TotalPositionDto> positions) {
    return countPositionTotals(positions.stream()
        .map(TotalPositionDto::getCurrentPosition)
        .collect(toList()));
  }

  @Named("countPreviousPositionTotals")
  default PositionDetailsTotalsDto countPreviousPositionTotals(List<TotalPositionDto> positions) {
    return countPositionTotals(positions.stream()
        .map(TotalPositionDto::getPreviousPosition)
        .collect(toList()));
  }

  default PositionDetailsTotalsDto countPositionTotals(List<ParticipantPositionDto> positions) {
    return new PositionDetailsTotalsDto(
        positions.stream()
            .map(ParticipantPositionDto::getCredit)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO),
        positions.stream()
            .map(ParticipantPositionDto::getDebit)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
    );
  }

  @Named("countIntraDayTotals")
  default IntraDayPositionTotalDto countIntraDayTotals(List<IntraDayPositionGross> intraDays) {
    if (intraDays == null) {
      return IntraDayPositionTotalDto.builder().build();
    }
    return IntraDayPositionTotalDto.builder()
        .totalDebitCap(intraDays.stream()
            .map(i -> i.getDebitCapAmount().getAmount())
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
        .totalDebitPosition(intraDays.stream()
            .map(i -> i.getDebitPositionAmount().getAmount())
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
        .build();
  }

  @Mappings({
      @Mapping(target = "name", source = "fileName"),
      @Mapping(target = "createdAt", source = "createdDate"),
      @Mapping(target = "senderBic", source = "originator")
  })
  FileDto toDto(File file);

  @Mapping(target = "id", source = "batchId")
  BatchDto toDto(Batch batch);

  @Mappings({
      @Mapping(target = "status", source = "batch.status"),
      @Mapping(target = "fileName", source = "batch.fileName"),
      @Mapping(target = "reasonCode", source = "batch.reasonCode"),
      @Mapping(target = "messageType", source = "batch.messageType"),
      @Mapping(target = "settlementDate", source = "batch.settlementDate"),
      @Mapping(target = "fileSize", source = "file.fileSize"),
      @Mapping(target = "sender.entityName", source = "batch.senderBank"),
      @Mapping(target = "sender.entityBic", source = "batch.senderBic"),
      @Mapping(target = "sender.iban", source = "batch.senderIban")
  })
  BatchDetailsDto toDetailsDto(Batch batch, File file);

  PageDto<ParticipantSettlementCycleDto> toDto(Page<ParticipantSettlement> settlements,
      @Context List<Participant> participants);

  @Mappings({
      @Mapping(target = "participant", source = "settlement.schemeParticipantIdentifier", qualifiedByName = "findParticipant"),
      @Mapping(target = "settlementTime", source = "settlement.settlementStartDate")
  })
  ParticipantSettlementCycleDto toDto(@Context List<Participant> participants,
      ParticipantSettlement settlement);

  @Mappings({
      @Mapping(target = "cycleId", expression = "java(settlementDetailsPage.getItems().get(0).getCycleId())"),
      @Mapping(target = "settlementTime", expression = "java(settlementDetailsPage.getItems().get(0).getSettlementCycleDate())"),
      @Mapping(target = "status", expression = "java(settlementDetailsPage.getItems().get(0).getStatus())"),
      @Mapping(target = "participant", source = "participant"),
      @Mapping(target = "instructions", source = "settlementDetailsPage")
  })
  ParticipantSettlementDetailsDto toDto(Page<SettlementDetails> settlementDetailsPage,
      @Context List<Participant> participants, Participant participant);

  @Mappings({
      @Mapping(target = "reference", source = "settlementDetails.settlementInstructionReference", qualifiedByName = "fromIntegerToString"),
      @Mapping(target = "status", source = "settlementDetails.statusDetail"),
      @Mapping(target = "counterparty", source = "settlementDetails.counterParty", qualifiedByName = "findParticipant"),
      @Mapping(target = "settlementCounterparty", source = "settlementDetails.counterPartySettlement", qualifiedByName = "findParticipant"),
      @Mapping(target = "totalDebit", source = "settlementDetails.totalAmountDebited.amount"),
      @Mapping(target = "totalCredit", source = "settlementDetails.totalAmountCredited.amount")
  })
  ParticipantInstructionDto toDto(SettlementDetails settlementDetails, @Context List<Participant> participants);

  @Mappings({
      @Mapping(target = "cycleId", expression = "java(settlementDetailsPage.getItems().get(0).getCycleId())"),
      @Mapping(target = "settlementTime", expression = "java(settlementDetailsPage.getItems().get(0).getSettlementCycleDate())"),
      @Mapping(target = "status", expression = "java(settlementDetailsPage.getItems().get(0).getStatus())"),
      @Mapping(target = "participant", source = "participant"),
      @Mapping(target = "settlementBank", source = "settlementBank"),
      @Mapping(target = "instructions", source = "settlementDetailsPage")
  })
  ParticipantSettlementDetailsDto toDto(Page<SettlementDetails> settlementDetailsPage,
      @Context List<Participant> participants, Participant participant, Participant settlementBank);

  @Named("fromIntegerToString")
  default String fromIntegerToString(Integer reference) {
    return String.valueOf(reference);
  }

  @Named("findParticipant")
  default ParticipantReferenceDto findParticipant(String participantId,
      @Context List<Participant> participants) {
    return participants.stream()
        .filter(p -> p.getBic().equals(participantId))
        .findFirst()
        .map(this::toReferenceDto)
        .orElse(null);
  }

  SettlementCycleScheduleDto toDto(SettlementCycleSchedule schedule);

  @Mappings({
      @Mapping(target = "amount", source = "amount.amount"),
      @Mapping(target = "senderBic", source = "originator")
  })
  TransactionDto toDto(Transaction transaction);

  @Mappings({
      @Mapping(target = "currency", source = "transaction.amount.currency"),
      @Mapping(target = "amount", source = "transaction.amount.amount")
  })
  TransactionDetailsDto toDetailsDto(Transaction transaction);

  EnquirySenderDetailsDto toDto(EnquirySenderDetails senderDetails);

  AlertPriorityDataDto toDto(AlertPriorityData priorityData);

  @Mappings({
      @Mapping(target = "customerCreditTransfer", source = "position", qualifiedByName = "toCustomerCreditTransfer"),
      @Mapping(target = "paymentReturn", source = "position", qualifiedByName = "toPaymentReturn")
  })
  PositionDetailsDto toPositionDetailsDto(ParticipantPosition position);

  @Named("toCustomerCreditTransfer")
  default ParticipantPositionDto toCustomerCreditTransfer(ParticipantPosition position) {
    BigDecimal debit = position.getPaymentSent().getAmount().getAmount();
    BigDecimal credit = position.getPaymentReceived().getAmount().getAmount();

    return new ParticipantPositionDto(credit, debit, credit.subtract(debit));
  }

  @Named("toPaymentReturn")
  default ParticipantPositionDto toPaymentReturn(ParticipantPosition position) {
    BigDecimal debit = position.getReturnSent().getAmount().getAmount();
    BigDecimal credit = position.getReturnReceived().getAmount().getAmount();

    return new ParticipantPositionDto(credit, debit, credit.subtract(debit));
  }

  @Mappings({
      @Mapping(target = "credit", source = "position", qualifiedByName = "countCredit"),
      @Mapping(target = "debit", source = "position", qualifiedByName = "countDebit"),
      @Mapping(target = "netPosition", source = "netPositionAmount.amount")
  })
  ParticipantPositionDto toDto(ParticipantPosition position);

  @Named("countCredit")
  default BigDecimal countCredit(ParticipantPosition position) {
    return position.getPaymentReceived().getAmount().getAmount()
        .add(position.getReturnReceived().getAmount().getAmount());
  }

  @Named("countDebit")
  default BigDecimal countDebit(ParticipantPosition position) {
    return position.getPaymentSent().getAmount().getAmount()
        .add(position.getReturnSent().getAmount().getAmount());
  }

  @Mappings({
      @Mapping(target = "totalCredit", source = "position", qualifiedByName = "countTotalCredit"),
      @Mapping(target = "totalDebit", source = "position", qualifiedByName = "countTotalDebit"),
      @Mapping(target = "totalNetPosition", source = "netPositionAmount.amount")
  })
  PositionDetailsTotalsDto toTotalPositionDetailsDto(ParticipantPosition position);

  @Named("countTotalCredit")
  default BigDecimal countTotalCredit(ParticipantPosition position) {
    BigDecimal paymentCredit = position.getPaymentReceived().getAmount().getAmount();
    BigDecimal returnCredit = position.getReturnReceived().getAmount().getAmount();
    return paymentCredit.add(returnCredit);
  }

  @Named("countTotalDebit")
  default BigDecimal countTotalDebit(ParticipantPosition position) {
    BigDecimal paymentDebit = position.getPaymentSent().getAmount().getAmount();
    BigDecimal returnDebit = position.getReturnSent().getAmount().getAmount();
    return paymentDebit.add(returnDebit);
  }

  @Mappings({
      @Mapping(target = "participant", source = "participant"),
      @Mapping(target = "currentCycle", source = "currentCycle"),
      @Mapping(target = "previousCycle", source = "previousCycle"),
      @Mapping(target = "currentPosition", source = "currentPosition"),
      @Mapping(target = "previousPosition", source = "previousPosition"),
      @Mapping(target = "previousPositionTotals", source = "previousPosition"),
      @Mapping(target = "currentPositionTotals", source = "currentPosition")
  })
  ParticipantDashboardSettlementDetailsDto toDto(Cycle currentCycle, Cycle previousCycle,
      ParticipantPosition currentPosition, ParticipantPosition previousPosition,
      Participant participant);

  @Mappings({
      @Mapping(target = "participant", source = "participant"),
      @Mapping(target = "currentCycle", source = "currentCycle"),
      @Mapping(target = "previousCycle", source = "previousCycle"),
      @Mapping(target = "currentPosition", source = "currentPosition"),
      @Mapping(target = "previousPosition", source = "previousPosition"),
      @Mapping(target = "previousPositionTotals", source = "previousPosition"),
      @Mapping(target = "currentPositionTotals", source = "currentPosition"),
      @Mapping(target = "settlementBank", source = "fundingParticipant"),
      @Mapping(target = "intraDayPositionGross", source = "intradayPositionGross")
  })
  ParticipantDashboardSettlementDetailsDto toDto(Cycle currentCycle, Cycle previousCycle,
      ParticipantPosition currentPosition, ParticipantPosition previousPosition,
      Participant participant, Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross);

  PageDto<ApprovalDetailsDto> toApprovalDetailsDto(Page<Approval> approval, @Context List<Participant> participants);

  @Mappings({
      @Mapping(target = "createdAt", source = "date"),
      @Mapping(target = "jobId", source = "approvalId"),
      @Mapping(target = "participants", source = "participantIds", qualifiedByName = "getApprovalReferenceParticipants")
  })
  ApprovalDetailsDto toDto(Approval approval, @Context List<Participant> participants);

  @Mappings({
      @Mapping(target = "filesRejected", source = "ioDashboard.fileRejected", qualifiedByName = "removePercent"),
      @Mapping(target = "batchesRejected", source = "ioDashboard.batchesRejected", qualifiedByName = "removePercent"),
      @Mapping(target = "transactionsRejected", source = "ioDashboard.transactionsRejected", qualifiedByName = "removePercent"),
      @Mapping(target = "dateFrom", source = "date"),
      @Mapping(target = "rows", source = "ioDashboard.summary")
  })
  IODashboardDto toDto(IODashboard ioDashboard, @Context List<Participant> participants, LocalDate date);

  @Mappings({
      @Mapping(target = "participant", source = "participantIOData.schemeParticipantIdentifier", qualifiedByName = "getParticipantById")
  })
  ParticipantIODataDto toDto(ParticipantIOData participantIOData, @Context List<Participant> participants);

  ParticipantDto toDto(Participant participant);

  @Mappings({
      @Mapping(target = "rejected", source = "rejected", qualifiedByName = "getRejectedFromString")
  })
  IODataDto toDto(IOData ioData);

  @Named("getParticipantById")
  default ParticipantDto getParticipantById(String schemeParticipantIdentifier, @Context List<Participant> participants) {
    return participants.stream()
        .filter(p -> p.getId().equals(schemeParticipantIdentifier))
        .map(this::toDto)
        .findFirst().orElseThrow(() -> new NoSuchElementException("No such participant in list"));
  }

  @Named("getRejectedFromString")
  default Double getRejectedFromString(String rejected) {
    return Double.valueOf(rejected.replaceAll("%", ""));
  }

//  TODO: check if percent symbol is needed in response
  @Named("removePercent")
  default String removePercent(String totalRejected) {
    return totalRejected.replaceAll("%", "");
  }

  @Named("getApprovalReferenceParticipants")
  default List<ParticipantReferenceDto> getApprovalReferenceParticipants(List<String> participantIds, @Context List<Participant> participants) {
    return participants.stream()
        .filter(p -> participantIds.contains(p.getId()))
        .map(this::toReferenceDto)
        .sorted(comparing((ParticipantReferenceDto p) -> !p.getParticipantType()
            .contains(FUNDING.getDescription()))
            .thenComparing(ParticipantReferenceDto::getName))
        .skip(0)
        .collect(toList());
  }

  @Mapping(target = "recipients", ignore = true)
  BroadcastDto toDto(Broadcast broadcast);

  ReportDto toDto(Report report);

  RoutingRecordDto toDto(RoutingRecord routingRecord);

  @Mappings({
      @Mapping(target = "bic", source = "participant.bic"),
      @Mapping(target = "fundingBic", source = "participant.fundingBic"),
      @Mapping(target = "id", source = "participant.id"),
      @Mapping(target = "name", source = "participant.name"),
      @Mapping(target = "status", source = "participant.status"),
      @Mapping(target = "suspendedTime", source = "participant.suspendedTime"),
      @Mapping(target = "participantType", source = "participant.participantType"),
      @Mapping(target = "organizationId", source = "participant.organizationId"),
      @Mapping(target = "tpspName", source = "participant.tpspName"),
      @Mapping(target = "tpspId", source = "participant.tpspId"),
      @Mapping(target = "fundingParticipant", source = "fundingParticipant"),
      @Mapping(target = "fundedParticipants", source = "participant.fundedParticipants"),
      @Mapping(target = "outputTxnVolume", source = "configuration.txnVolume"),
      @Mapping(target = "outputTxnTimeLimit", source = "configuration.outputFileTimeLimit"),
      @Mapping(target = "debitCapLimit", source = "configuration.debitCapLimit"),
      @Mapping(target = "debitCapLimitThresholds", source = "configuration.debitCapLimitThresholds"),
      @Mapping(target = "outputChannel", source = "configuration.networkName"),
      @Mapping(target = "settlementAccountNo", source = "account.accountNo"),
  })
  ManagedParticipantDetailsDto toDto(Participant participant,
      ParticipantConfiguration configuration, Participant fundingParticipant, Account account);

  @Mappings({
      @Mapping(target = "outputTxnVolume", source = "configuration.txnVolume"),
      @Mapping(target = "outputTxnTimeLimit", source = "configuration.outputFileTimeLimit"),
      @Mapping(target = "debitCapLimit", source = "configuration.debitCapLimit"),
      @Mapping(target = "outputChannel", source = "configuration.networkName"),
      @Mapping(target = "settlementAccountNo", source = "account.accountNo"),
  })
  ManagedParticipantDetailsDto toDto(Participant participant,
      ParticipantConfiguration configuration, Account account);

  @Mappings({
      @Mapping(target = "id", source = "userId"),
      @Mapping(target = "participantName", source = "participantId"),
      @Mapping(target = "name", expression = "java(userDetails.getFirstName() + \" \" + userDetails.getLastName())")
  })
  ApprovalUserDto toDto(UserDetails userDetails);

  AlertDto toDto(Alert alert);

  @Mappings({
      @Mapping(target = "permissions", source = "permissions"),
      @Mapping(target = "participation", source = "participant"),
      @Mapping(target = "user.userId", source = "auditDetails.username"),
      @Mapping(target = "user.name", expression = "java(auditDetails.getFirstName() + \" \" + auditDetails.getLastName())")
  })
  CurrentUserInfoDto toDto(Participant participant, List<String> permissions, AuditDetails auditDetails);

  //TODO: check ManagedParticipant != Participant?
  ManagedParticipantDto toManagedDto(Participant participants);

  default <T> PageDto<T> toDto(Page<?> page, Class<T> targetType) {
    List<?> sourceItems = page.getItems();

    if (sourceItems == null || sourceItems.isEmpty()) {
      return new PageDto<>(0, emptyList());
    }

    final Class<?> sourceType = sourceItems.get(0).getClass();

    Method method = stream(this.getClass().getDeclaredMethods())
        .filter(m -> stream(m.getParameterTypes()).anyMatch(p -> p.isAssignableFrom(sourceType)))
        .filter(m -> m.getReturnType().isAssignableFrom(targetType))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Method by signature not found!"));

    final List<T> targetItems = sourceItems.stream()
        .map(sourceType::cast)
        .map(obj -> {
          try {
            return method.invoke(this, obj);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UILayerException(e.getCause(), "DataTransferObject mapping exception!");
          }
        })
        .map(targetType::cast)
        .collect(toList());

    return new PageDto<>(page.getTotalResults(), targetItems);
  }

  ValidationApprovalDto toDto(ValidationApproval validationApproval);

  ConfigurationDto toDto(Configuration configuration, Integer dataRetentionDays, String timeZone);

  @Mappings({
      @Mapping(target = "createdAt", source = "timestamp"),
      @Mapping(target = "eventType", source = "userActivityString"),
      @Mapping(target = "user.name", source = "username"),
      @Mapping(target = "user.id", source = "participantId"),
      @Mapping(target = "user.participantName",
          expression = "java(auditDetails.getFirstName()"
              + ".concat(\" \")"
              + ".concat(auditDetails.getLastName()))"),
  })
  AuditDto toDto(AuditDetails auditDetails);

  UserActivityDto toDto(UserActivity userActivity);
}
