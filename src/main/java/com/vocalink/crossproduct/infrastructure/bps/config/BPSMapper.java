package com.vocalink.crossproduct.infrastructure.bps.config;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertPriorityType;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.alert.AlertStatsData;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.cycle.Cycle;
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
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.settlement.BPSInstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.BPSSettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlert;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertReferenceData;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStats;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertStatsData;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatch;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFile;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileReference;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOBatchesMessageTypes;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOData;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODataAmountDetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODataDetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIOTransactionsMessageTypes;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSInstructionEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionEnquirySearchRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BPSMapper {

  BPSMapper BPSMAPPER = Mappers.getMapper(BPSMapper.class);

  @Mappings({
      @Mapping(target = "id", source = "cycleId"),
      @Mapping(target = "cutOffTime", source = "fileSubmissionCutOffTime")
  })
  Cycle toEntity(BPSCycle cycle);

  @Mappings({
      @Mapping(target = "credit", source = "position", qualifiedByName = "countCredit"),
      @Mapping(target = "debit", source = "position", qualifiedByName = "countDebit"),
      @Mapping(target = "netPosition", source = "netPositionAmount.amount")
  })
  ParticipantPosition toEntity(BPSSettlementPosition position);

  IntraDayPositionGross toEntity(BPSIntraDayPositionGross intraDay);

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

  @Mappings({
      @Mapping(target = "participantIdentifier", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "participantType", source = "participantType", qualifiedByName = "convertParticipantType"),
      @Mapping(target = "connectingParticipantId", source = "connectingParty"),
  })
  ParticipantReference toReference(BPSParticipant bpsParticipant);

  @Named("convertParticipantType")
  default ParticipantType convertParticipantType(String participantType) {
    return ParticipantType.valueOf(participantType.replaceAll("[_+-]", "_"));
  }

  @Named("convertParticipantStatus")
  default ParticipantStatus convertParticipantStatus(String participantStatus) {
    return ParticipantStatus.valueOf(participantStatus);
  }

  @Mappings({
      @Mapping(target = "connectingParty", source = "connectingParty"),
      @Mapping(target = "participantType", source = "participantType")
  })
  BPSParticipantsSearchRequest toBps(String connectingParty, String participantType);

  BPSBatchEnquirySearchRequest toBps(BatchEnquirySearchCriteria criteria);

  BPSFileEnquirySearchRequest toBps(FileEnquirySearchCriteria criteria);

  BPSTransactionEnquirySearchRequest toBps(TransactionEnquirySearchCriteria criteria);

  Page<Batch> toBatchPageEntity(BPSPage<BPSBatch> batches);

  Page<File> toFilePageEntity(BPSPage<BPSFile> files);

  Page<Transaction> toTransactionPageEntity(BPSPage<BPSTransaction> transactions);

  Transaction toEntity(BPSTransaction transaction);

  FileReference toEntity(BPSFileReference reference);

  BPSAlertSearchRequest toBps(AlertSearchCriteria criteria);

  Page<Alert> toAlertPageEntity(BPSPage<BPSAlert> alerts);

  AlertStats toEntity(BPSAlertStats alertStats);

  AlertReferenceData toEntity(BPSAlertReferenceData alertReferenceData);

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

  @Mappings({
      @Mapping(target = "fileName", source = "name"),
      @Mapping(target = "settlementCycleId", source = "cycle.cycleId"),
      @Mapping(target = "settlementDate", source = "cycle.settlementTime", qualifiedByName = "convertToDate")
  })
  Batch toEntity(BPSBatch batch);

  @Mappings({
      @Mapping(target = "fileName", source = "name"),
      @Mapping(target = "settlementCycleId", source = "cycle.cycleId"),
      @Mapping(target = "settlementDate", source = "cycle.settlementTime", qualifiedByName = "convertToDate")
  })
  File toEntity(BPSFile file);

  IOBatchesMessageTypes toEntity(BPSIOBatchesMessageTypes batchesMessageTypes);

  IOData toEntity(BPSIOData ioData);

  IODataAmountDetails toEntity(BPSIODataAmountDetails ioDataAmountDetails);

  IODataDetails toEntity(BPSIODataDetails ioDataDetails);

  IODetails toEntity(BPSIODetails ioDetails);

  IOTransactionsMessageTypes toEntity(BPSIOTransactionsMessageTypes transactionsMessageTypes);

  ParticipantIOData toEntity(BPSParticipantIOData participantIOData);

  @Named("countCredit")
  default BigDecimal countCredit(BPSSettlementPosition position) {
    return position.getPaymentReceived().getAmount().getAmount()
        .add(position.getReturnReceived().getAmount().getAmount());
  }

  @Named("countDebit")
  default BigDecimal countDebit(BPSSettlementPosition position) {
    return position.getPaymentSent().getAmount().getAmount()
        .add(position.getReturnSent().getAmount().getAmount());
  }

  @Named("convertToDate")
  default LocalDate convertToDate(ZonedDateTime date) {
    return date.toLocalDate();
  }

  BPSInstructionEnquiryRequest toBps(BPSInstructionEnquirySearchCriteria criteria);

  BPSSettlementEnquiryRequest toBps(BPSSettlementEnquirySearchCriteria criteria);
}
