package com.vocalink.crossproduct.infrastructure.adapter;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.Amount;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.AlertPriorityData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.Payment;
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference;
import com.vocalink.crossproduct.domain.settlement.BPSInstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.BPSSettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionStatus;
import com.vocalink.crossproduct.domain.settlement.ParticipantInstruction;
import com.vocalink.crossproduct.domain.settlement.ParticipantSettlement;
import com.vocalink.crossproduct.domain.settlement.SettlementSchedule;
import com.vocalink.crossproduct.domain.settlement.SettlementStatus;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertPriority;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSPayment;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSIODetails;
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIOData;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSSettlementPositionWrapper;
import com.vocalink.crossproduct.infrastructure.bps.reference.BPSMessageDirectionReference;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantInstruction;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSParticipantSettlement;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementSchedule;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransaction;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
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
  BPSInstructionEnquirySearchCriteria toEntity(ParticipantSettlementRequest request, String cycleId,
      String participantId);

  BPSSettlementEnquirySearchCriteria toEntity(SettlementEnquiryRequest request);

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

}
