package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.settlement.BPSInstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.BPSSettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantInstruction;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantSettlement;
import com.vocalink.crossproduct.shared.io.CPIODetails;
import com.vocalink.crossproduct.shared.io.CPParticipantIOData;
import com.vocalink.crossproduct.shared.settlement.SettlementStatus;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntityMapper {

  EntityMapper MAPPER = Mappers.getMapper(EntityMapper.class);

  IODetails toEntity(CPIODetails input);

  ParticipantIOData toEntity(CPParticipantIOData input);

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
    return settlements.stream().map(this::toEntity).collect(Collectors.toList());
  }

  @Named("toStatus")
  default SettlementStatus convertStatusType(String status) {
    return SettlementStatus.valueOf(status.toUpperCase().replaceAll("[_+-]", "_"));
  }

  @Mappings({
      @Mapping(target = "instructions", source = "instructions"),
      @Mapping(target = "instructions.items", source = "instructions.items", qualifiedByName = "doInstructions"),
      @Mapping(target = "status", source = "settlement.status", qualifiedByName = "toStatus")
  })
  ParticipantSettlement toEntity(BPSParticipantSettlement settlement,
      BPSPage<BPSParticipantInstruction> instructions);

  @Named("doInstructions")
  default List<ParticipantInstruction> doInstructions(List<BPSParticipantInstruction> instructions) {
    return instructions.stream().map(this::toEntity).collect(Collectors.toList());
  }

  @Mapping(source = "status", target = "status", qualifiedByName = "toStatus")
  ParticipantInstruction toEntity(BPSParticipantInstruction instruction);

  BatchEnquirySearchCriteria toEntity(BatchEnquirySearchRequest request);

  FileEnquirySearchCriteria toEntity(FileEnquirySearchRequest request);

  AlertSearchCriteria toEntity(AlertSearchRequest request);

  @Mappings({
      @Mapping(source = "cycleId", target = "cycleId"),
      @Mapping(source = "participantId", target = "participantId")
  })
  BPSInstructionEnquirySearchCriteria toEntity(ParticipantSettlementRequest request, String cycleId,
      String participantId);

  BPSSettlementEnquirySearchCriteria toEntity(SettlementEnquiryRequest request);

}
