package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileReference;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.shared.CPPage;
import com.vocalink.crossproduct.shared.alert.CPAlertReferenceData;
import com.vocalink.crossproduct.shared.files.CPFileReference;
import com.vocalink.crossproduct.shared.io.CPIODetails;
import com.vocalink.crossproduct.shared.io.CPParticipantIOData;
import com.vocalink.crossproduct.shared.participant.CPParticipant;
import com.vocalink.crossproduct.shared.settlement.CPInstructionEnquiryRequest;
import com.vocalink.crossproduct.shared.settlement.CPParticipantInstruction;
import com.vocalink.crossproduct.shared.settlement.CPParticipantSettlement;
import com.vocalink.crossproduct.shared.settlement.CPSettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

  EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

  IODetails toEntity(CPIODetails input);

  ParticipantIOData toEntity(CPParticipantIOData input);

  FileReference toEntity(CPFileReference cpFileReference);

  AlertReferenceData toEntity(CPAlertReferenceData alertReferenceData);

  @Mapping(target = "participantIdentifier", source = "id")
  ParticipantReference toCpReference(CPParticipant input);

  MessageDirectionReference toEntity(BPSMessageDirectionReference alertReferenceData);

  Page<ParticipantInstruction> toEntityInstruction(CPPage<CPParticipantInstruction> batches);

  ParticipantSettlement toEntity(CPParticipantSettlement settlement);

  Page<ParticipantSettlement> toEntity(CPPage<CPParticipantSettlement> settlements);

  CPInstructionEnquiryRequest toCp(ParticipantSettlementRequest request);

  @Mapping(target = "cycleIds", source = "cycleIds")
  @Mapping(target = "participants", source = "participants")
  CPSettlementEnquiryRequest toCp(SettlementEnquiryRequest request);

  BatchEnquirySearchCriteria toEntity(BatchEnquirySearchRequest request);

  FileEnquirySearchCriteria toEntity(FileEnquirySearchRequest request);

  AlertSearchCriteria toEntity(AlertSearchRequest request);
}
