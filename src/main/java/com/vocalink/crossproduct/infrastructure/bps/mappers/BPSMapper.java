package com.vocalink.crossproduct.infrastructure.bps.mappers;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
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
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertReferenceData;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalChangeRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatch;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.broadcasts.BPSBroadcast;
import com.vocalink.crossproduct.infrastructure.bps.broadcasts.BPSBroadcastsSearchRequest;
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
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecordRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSInstructionEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionEnquirySearchRequest;
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

  IntraDayPositionGross toEntity(BPSIntraDayPositionGross intraDay);

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

  FileReference toEntity(BPSFileReference reference);

  BPSAlertSearchRequest toBps(AlertSearchCriteria criteria);

  AlertReferenceData toEntity(BPSAlertReferenceData alertReferenceData);

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

  @Named("convertToDate")
  default LocalDate convertToDate(ZonedDateTime date) {
    return date.toLocalDate();
  }

  BPSInstructionEnquiryRequest toBps(InstructionEnquirySearchCriteria criteria);

  BPSSettlementEnquiryRequest toBps(SettlementEnquirySearchCriteria criteria);

  BPSManagedParticipantsSearchRequest toBps(ManagedParticipantsSearchCriteria criteria);

  BPSBroadcastsSearchRequest toBps(BroadcastsSearchCriteria criteria);

  BPSRoutingRecordRequest toBps(RoutingRecordCriteria criteria);

  Page<Broadcast> toPagedBroadcastEntity(BPSPage<BPSBroadcast> bpsBroadcastBPSPage);

  Broadcast toEntity(BPSBroadcast broadcast);

  @Mappings({
      @Mapping(target = "pageSize", source = "limit")
  })
  BPSApprovalSearchRequest toBps(ApprovalSearchCriteria criteria);

  @Mappings({
      @Mapping(target = "requestType", source = "requestType", qualifiedByName = "toBpsApprovalRequestType")
  })
  BPSApprovalChangeRequest toBps(ApprovalChangeCriteria criteria);

  @Named("toBpsApprovalRequestType")
  default BPSApprovalRequestType toBpsApprovalRequestType(ApprovalRequestType approvalRequestType) {
    return BPSApprovalRequestType.valueOf(approvalRequestType.name());
  }
}
