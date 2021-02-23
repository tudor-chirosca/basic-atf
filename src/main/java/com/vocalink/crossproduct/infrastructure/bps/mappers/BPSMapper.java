package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortParamMapper;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalChangeRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.broadcasts.BPSBroadcastsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.report.BPSReportSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecordRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSInstructionEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionEnquirySearchRequest;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BPSMapper {

  BPSMapper BPSMAPPER = Mappers.getMapper(BPSMapper.class);

  @Mappings({
      @Mapping(target = "connectingParty", source = "connectingParty"),
      @Mapping(target = "participantType", source = "participantType")
  })
  BPSParticipantsSearchRequest toBps(String connectingParty, String participantType);

  BPSBatchEnquirySearchRequest toBps(BatchEnquirySearchCriteria criteria);

  BPSFileEnquirySearchRequest toBps(FileEnquirySearchCriteria criteria);

  BPSTransactionEnquirySearchRequest toBps(TransactionEnquirySearchCriteria criteria);

  BPSAlertSearchRequest toBps(AlertSearchCriteria criteria);

  BPSInstructionEnquiryRequest toBps(InstructionEnquirySearchCriteria criteria);

  BPSSettlementEnquiryRequest toBps(SettlementEnquirySearchCriteria criteria);

  BPSManagedParticipantsSearchRequest toBps(ManagedParticipantsSearchCriteria criteria);

  BPSBroadcastsSearchRequest toBps(BroadcastsSearchCriteria criteria);

  BPSRoutingRecordRequest toBps(RoutingRecordCriteria criteria);

  BPSReportSearchRequest toBps(ReportSearchCriteria criteria);

  @Mappings({
      @Mapping(target = "sortingOrder", source = "sort", qualifiedByName = "mapApprovalSortParam")
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

  @Named("mapApprovalSortParam")
  default List<BPSSortingQuery> mapApprovalSortParam(List<String> sortParams) {
    if (sortParams == null || sortParams.isEmpty()) {
      return new ArrayList<>();
    }
    return sortParams.stream()
        .map(BPSSortParamMapper::getApprovalSortParam)
        .filter(s -> nonNull(s.getSortOrderBy()))
        .collect(toList());
  }
}
