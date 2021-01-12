package com.vocalink.crossproduct.infrastructure.bps.config;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatch;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFile;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileReference;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
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

  BPSBatchEnquirySearchRequest toBps(BatchEnquirySearchCriteria request);

  BPSFileEnquirySearchRequest toBps(FileEnquirySearchCriteria request);

  Page<Batch> toBatchPageEntity(BPSPage<BPSBatch> batches);

  Page<File> toFilePageEntity(BPSPage<BPSFile> files);

  FileReference toEntity(BPSFileReference reference);

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
}
