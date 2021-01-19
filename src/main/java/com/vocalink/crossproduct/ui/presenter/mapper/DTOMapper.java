package com.vocalink.crossproduct.ui.presenter.mapper;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertPriorityData;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.settlement.InstructionStatus;
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertPriorityDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto;
import com.vocalink.crossproduct.ui.dto.file.FileDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto;
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDetailsDto;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

  CycleDto toDto(Cycle cycle);

  AlertReferenceDataDto toDto(AlertReferenceData alertReferenceData);

  AlertStatsDto toDto(AlertStats alertStats);

  PageDto<AlertDto> toAlertPageDto(Page<Alert> alert);

  MessageDirectionReferenceDto toDto(MessageDirectionReference alert);

  @Mapping(target = "dateFrom", source = "date")
  IODetailsDto toDto(IODetails ioDetails, Participant participant, LocalDate date);

  ParticipantDto toDto(Participant participant);

  @Mappings({
      @Mapping(target = "participantIdentifier", source = "id"),
      @Mapping(target = "connectingParticipantId", source = "fundingBic")
  })
  ParticipantReference toReference(Participant input);

  @Mappings({
      @Mapping(target = "participantIdentifier", source = "id"),
      @Mapping(target = "connectingParticipantId", source = "fundingBic")
  })
  ParticipantReferenceDto toReferenceDto(Participant participant);

  ParticipantReferenceDto toDto(ParticipantReference participant);

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

  List<FileStatusesTypeDto> toDtoType(List<FileReference> fileReferences);

  PageDto<FileDto> toFilePageDto(Page<File> files);

  @Mapping(target = "name", source = "fileName")
  @Mapping(target = "senderBic", source = "sender.entityBic")
  FileDto toDto(File file);

  PageDto<BatchDto> toBatchPageDto(Page<Batch> batches);

  @Mapping(target = "id", source = "batchId")
  @Mapping(target = "senderBic", source = "sender.entityBic")
  BatchDto toDto(Batch batch);

  FileDetailsDto toDetailsDto(File file);

  BatchDetailsDto toDetailsDto(Batch batch);

  PageDto<ParticipantSettlementCycleDto> toDto(Page<ParticipantSettlement> settlements, @Context List<Participant> participants);

  @Mapping(target = "participant", source = "settlement.participantId", qualifiedByName = "findParticipant")
  ParticipantSettlementCycleDto toDto(@Context List<Participant> participants, ParticipantSettlement settlement);

  @Mapping(target = "participant", source = "settlement.participantId", qualifiedByName = "findParticipant")
  ParticipantSettlementDetailsDto toDto(ParticipantSettlement settlement, @Context  List<Participant> participants);

  @Mapping(target = "counterparty", source = "counterpartyId", qualifiedByName = "findParticipant")
  @Mapping(target = "settlementCounterparty", source = "settlementCounterpartyId", qualifiedByName = "findParticipant")
  @Mapping(target = "status", source = "status", qualifiedByName = "toStatus")
  ParticipantInstructionDto toDto(ParticipantInstruction participantInstruction, @Context List<Participant> participants);

  @Named("toStatus")
  default InstructionStatus convertStatusType(String status) {
    return InstructionStatus.valueOf(status.toUpperCase().replaceAll("[_+-]", "_"));
  }

  @Named("findParticipant")
  default ParticipantReferenceDto findParticipant(String participantId, @Context List<Participant> participants) {
    return participants.stream()
        .filter(p -> p.getBic().equals(participantId))
        .findFirst()
        .map(this::toReferenceDto)
        .orElse(null);
  }

  SettlementScheduleDto toDto(SettlementSchedule schedule);

  PageDto<TransactionDto> toTransactionPageDto(Page<Transaction> transactions);

  @Mappings({
      @Mapping(target = "amount", source = "amount.amount"),
      @Mapping(target = "senderBic", source = "senderEntityBic")
  })
  TransactionDto toDto(Transaction transaction);

  @Mappings({
      @Mapping(target = "amount", source = "amount.amount"),
      @Mapping(target = "sender", source = "transaction", qualifiedByName = "toSender"),
      @Mapping(target = "receiver", source = "transaction", qualifiedByName = "toReceiver")
  })
  TransactionDetailsDto toDetailsDto(Transaction transaction);

  @Named("toReceiver")
  @Mappings({
      @Mapping(target = "entityName", source = "receiverEntityName"),
      @Mapping(target = "entityBic", source = "receiverEntityBic"),
      @Mapping(target = "iban", source = "receiverIban")
  })
  EnquirySenderDetailsDto toReceiver(Transaction transaction);

  @Named("toSender")
  @Mappings({
      @Mapping(target = "entityName", source = "senderEntityName"),
      @Mapping(target = "entityBic", source = "senderEntityBic"),
      @Mapping(target = "iban", source = "senderIban"),
      @Mapping(target = "fullName", source = "senderFullName")
  })
  EnquirySenderDetailsDto toSender(Transaction transaction);

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
      @Mapping(target = "previousPositionTotals", source = "currentPosition"),
      @Mapping(target = "currentPositionTotals", source = "previousPosition")
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
      @Mapping(target = "previousPositionTotals", source = "currentPosition"),
      @Mapping(target = "currentPositionTotals", source = "previousPosition"),
      @Mapping(target = "settlementBank", source = "fundingParticipant"),
      @Mapping(target = "intraDayPositionGross", source = "intradayPositionGross")
  })
  ParticipantDashboardSettlementDetailsDto toDto(Cycle currentCycle, Cycle previousCycle,
      ParticipantPosition currentPosition, ParticipantPosition previousPosition,
      Participant participant, Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross);
}
