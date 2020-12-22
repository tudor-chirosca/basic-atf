package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.adapter.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.files.File;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.shared.CPPage;
import com.vocalink.crossproduct.shared.alert.CPAlert;
import com.vocalink.crossproduct.shared.alert.CPAlertReferenceData;
import com.vocalink.crossproduct.shared.alert.CPAlertStats;
import com.vocalink.crossproduct.shared.batch.CPBatch;
import com.vocalink.crossproduct.shared.batch.CPBatchEnquirySearchRequest;
import com.vocalink.crossproduct.shared.cycle.CPCycle;
import com.vocalink.crossproduct.shared.files.CPFile;
import com.vocalink.crossproduct.shared.files.CPFileEnquirySearchRequest;
import com.vocalink.crossproduct.shared.files.CPFileReference;
import com.vocalink.crossproduct.shared.io.CPIODetails;
import com.vocalink.crossproduct.shared.io.CPParticipantIOData;
import com.vocalink.crossproduct.shared.participant.CPParticipant;
import com.vocalink.crossproduct.shared.participant.CPParticipantsSearchRequest;
import com.vocalink.crossproduct.shared.positions.CPIntraDayPositionGross;
import com.vocalink.crossproduct.shared.positions.CPPositionDetails;
import com.vocalink.crossproduct.shared.reference.CPMessageDirectionReference;
import com.vocalink.crossproduct.shared.settlement.CPInstructionEnquiryRequest;
import com.vocalink.crossproduct.shared.settlement.CPParticipantInstruction;
import com.vocalink.crossproduct.shared.settlement.CPParticipantSettlement;
import com.vocalink.crossproduct.shared.settlement.CPSettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

  EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

  Cycle toEntity(CPCycle input);

  IntraDayPositionGross toEntity(CPIntraDayPositionGross input);

  IODetails toEntity(CPIODetails input);

  ParticipantIOData toEntity(CPParticipantIOData input);

  PositionDetails toEntity(CPPositionDetails input);

  Participant toEntity(CPParticipant input);

  FileReference toEntity(CPFileReference cpFileReference);

  AlertReferenceData toEntity(CPAlertReferenceData alertReferenceData);

  Page<Alert> toEntityAlert(CPPage<CPAlert> input);

  @Mapping(target = "participantIdentifier", source = "id")
  ParticipantReference toCpReference(CPParticipant input);

  AlertStats toEntity(CPAlertStats alertReferenceData);

  MessageDirectionReference toEntity(CPMessageDirectionReference alertReferenceData);

  Page<File> toEntityFile(CPPage<CPFile> files);

  Page<Batch> toEntityBatch(CPPage<CPBatch> batches);

  Page<ParticipantInstruction> toEntityInstruction(CPPage<CPParticipantInstruction> batches);

  ParticipantSettlement toEntity(CPParticipantSettlement settlement);

  Page<ParticipantSettlement> toEntity(CPPage<CPParticipantSettlement> settlements);

  @Mapping(target = "fileName", source = "name")
  @Mapping(target = "settlementCycleId", source = "cycle.id")
  @Mapping(target = "settlementDate", source = "cycle.settlementTime", qualifiedByName = "convertToDate")
  Batch toEntity(CPBatch file);

  CPFileEnquirySearchRequest toCp(FileEnquirySearchRequest request);

  CPBatchEnquirySearchRequest toCp(BatchEnquirySearchRequest request);

  CPInstructionEnquiryRequest toCp(ParticipantSettlementRequest request);

  @Mapping(target = "connectingParty", source = "connectingParty")
  @Mapping(target = "participantType", source = "participantType")
  CPParticipantsSearchRequest toCp(String connectingParty, String participantType);

  @Mapping(target = "cycleIds", source = "cycleIds")
  @Mapping(target = "participants", source = "participants")
  CPSettlementEnquiryRequest toCp(SettlementEnquiryRequest request);

  @Mapping(target = "fileName", source = "name")
  @Mapping(target = "settlementCycleId", source = "cycle.id")
  @Mapping(target = "settlementDate", source = "cycle.settlementTime", qualifiedByName = "convertToDate")
  File toEntity(CPFile file);

  @Named("convertToDate")
  default LocalDate convertToDate(LocalDateTime date) {
    return date.toLocalDate();
  }
}
