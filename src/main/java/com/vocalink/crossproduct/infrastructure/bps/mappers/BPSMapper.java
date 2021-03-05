package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static com.vocalink.crossproduct.infrastructure.bps.BPSSortParamMapper.resolveParams;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.MapperUtils.getApprovalSearchRequestSortParams;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.MapperUtils.getBatchSearchRequestSortParams;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.MapperUtils.getFileSearchRequestSortParams;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.MapperUtils.getTransactionSearchRequestSortParams;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalChangeCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmation;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalSearchCriteria;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.domain.files.FileEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.domain.settlement.InstructionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.settlement.SettlementEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import com.vocalink.crossproduct.infrastructure.bps.alert.BPSAlertSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalChangeRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalConfirmationRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalRequestType;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.approval.BPSApprovalStatus;
import com.vocalink.crossproduct.infrastructure.bps.batch.BPSBatchEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.broadcasts.BPSBroadcastsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.file.BPSFileEnquirySearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.report.BPSReportSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.routing.BPSRoutingRecordRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSInstructionEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.settlement.BPSSettlementEnquiryRequest;
import com.vocalink.crossproduct.infrastructure.bps.transaction.BPSTransactionEnquirySearchRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

  @Mappings({
      @Mapping(target = "createdFromDate", source = "dateFrom", qualifiedByName = "toZonedDateTimeConverter-SOD"),
      @Mapping(target = "createdToDate", source = "dateTo", qualifiedByName = "toZonedDateTimeConverter-EOD"),
      @Mapping(target = "sendingParticipant", source = "sendingBic"),
      @Mapping(target = "sessionInstanceId", source = "cycleId"),
      @Mapping(target = "receivingParticipant", source = "receivingBic"),
      @Mapping(target = "identifier", source = "id"),
      @Mapping(target = "sortingOrder", source = "sort", qualifiedByName = "mapBatchSortParams")
  })
  BPSBatchEnquirySearchRequest toBps(BatchEnquirySearchCriteria criteria);

  @AfterMapping
  default void updateRequest(@MappingTarget BPSBatchEnquirySearchRequest request) {
    if(request.getSessionInstanceId() != null && !request.getSessionInstanceId().isEmpty()) {
      request.setCreatedFromDate(null);
      request.setCreatedToDate(null);
    }
  }

  @Named("mapBatchSortParams")
  default List<BPSSortingQuery> mapBatchSortParams(List<String> sortParams) {
    return map(sortParams, getBatchSearchRequestSortParams());
  }

  @Mappings({
      @Mapping(target = "createdFromDate", source = "dateFrom", qualifiedByName = "toZonedDateTimeConverter-SOD"),
      @Mapping(target = "createdToDate", source = "dateTo", qualifiedByName = "toZonedDateTimeConverter-EOD"),
      @Mapping(target = "sessionInstanceId", source = "cycleId"),
      @Mapping(target = "sendingParticipant", source = "sendingBic"),
      @Mapping(target = "receivingParticipant", source = "receivingBic"),
      @Mapping(target = "identifier", source = "id"),
      @Mapping(target = "sortingOrder", source = "sort", qualifiedByName = "mapFileSortParams")
  })
  BPSFileEnquirySearchRequest toBps(FileEnquirySearchCriteria criteria);

  @AfterMapping
  default void updateRequest(@MappingTarget BPSFileEnquirySearchRequest request) {
    if(request.getSessionInstanceId() != null && !request.getSessionInstanceId().isEmpty()) {
      request.setCreatedFromDate(null);
      request.setCreatedToDate(null);
    }
  }

  @Named("toZonedDateTimeConverter-SOD")
  default ZonedDateTime toZonedDateTimeSOD(LocalDate localDate) {
    return localDate == null ? null : ZonedDateTime.of(localDate, LocalTime.MIN, ZoneId.of("UTC"));
  }

  @Named("toZonedDateTimeConverter-EOD")
  default ZonedDateTime toZonedDateTimeEOD(LocalDate localDate) {
    return localDate == null ? null : ZonedDateTime.of(localDate, LocalTime.of(23, 59, 59), ZoneId.of("UTC"));
  }

  @Named("mapFileSortParams")
  default List<BPSSortingQuery> mapFileSortParams(List<String> sortParams) {
    return map(sortParams, getFileSearchRequestSortParams());
  }

  @Mappings({
      @Mapping(target = "createdDateFrom", source = "dateFrom"),
      @Mapping(target = "createdDateTo", source = "dateTo"),
      @Mapping(target = "sendingParticipant", source = "sendingBic"),
      @Mapping(target = "receivingParticipant", source = "receivingBic"),
      @Mapping(target = "instructionIdentifier", source = "id"),
      @Mapping(target = "transactionRangeFrom", source = "txnFrom", qualifiedByName = "mapToBpsAmount"),
      @Mapping(target = "transactionRangeTo", source = "txnTo", qualifiedByName = "mapToBpsAmount"),
      @Mapping(target = "sortingOrder", source = "sort", qualifiedByName = "mapTransactionSortParams")
  })
  BPSTransactionEnquirySearchRequest toBps(TransactionEnquirySearchCriteria criteria, @Context String currency);

  @Named("mapToBpsAmount")
  default BPSAmount mapToBpsAmount(BigDecimal amount, @Context String currency) {
    if (amount == null) {
      return null;
    }
    return new BPSAmount(amount, currency);
  }

  @Named("mapTransactionSortParams")
  default List<BPSSortingQuery> mapTransactionSortParams(List<String> sortParams) {
    return map(sortParams, getTransactionSearchRequestSortParams());
  }

  BPSAlertSearchRequest toBps(AlertSearchCriteria criteria);

  BPSInstructionEnquiryRequest toBps(InstructionEnquirySearchCriteria criteria);

  BPSSettlementEnquiryRequest toBps(SettlementEnquirySearchCriteria criteria);

  BPSManagedParticipantsSearchRequest toBps(ManagedParticipantsSearchCriteria criteria);

  BPSBroadcastsSearchRequest toBps(BroadcastsSearchCriteria criteria);

  BPSRoutingRecordRequest toBps(RoutingRecordCriteria criteria);

  BPSReportSearchRequest toBps(ReportSearchCriteria criteria);

  @Mappings({
      @Mapping(target = "sortingOrder", source = "sort", qualifiedByName = "mapApprovalSortParams"),
      @Mapping(target = "approvalId", source = "jobId"),
      @Mapping(target = "schemeParticipantIdentifiers", source = "participantIds"),
      @Mapping(target = "requestTypes", source = "requestTypes", qualifiedByName = "toBpsApprovalRequestType"),
      @Mapping(target = "statuses", source = "statuses", qualifiedByName = "toBpsApprovalStatus"),
  })
  BPSApprovalSearchRequest toBps(ApprovalSearchCriteria criteria);

  @Mappings({
      @Mapping(target = "requestType", source = "requestType", qualifiedByName = "toBpsApprovalRequestType")
  })
  BPSApprovalChangeRequest toBps(ApprovalChangeCriteria criteria);

  @Mappings({
      @Mapping(target = "notes", source = "message"),
      @Mapping(target = "isApproved", source = "action", qualifiedByName = "getIsApprovedFromType")
  })
  BPSApprovalConfirmationRequest toBps(ApprovalConfirmation approvalConfirmation);

  @Named("getIsApprovedFromType")
  default boolean getIsApprovedFromType(ApprovalConfirmationType confirmationType) {
    return confirmationType.equals(ApprovalConfirmationType.APPROVE);
  }

  @Named("toBpsApprovalRequestType")
  default BPSApprovalRequestType toBpsApprovalRequestType(ApprovalRequestType approvalRequestType) {
    return BPSApprovalRequestType.valueOf(approvalRequestType.name());
  }

  @Named("toBpsApprovalStatus")
  default BPSApprovalStatus toBpsApprovalStatus(ApprovalStatus approvalStatus) {
    return BPSApprovalStatus.valueOf(approvalStatus.name());
  }

  @Named("mapApprovalSortParams")
  default List<BPSSortingQuery> mapApprovalSortParam(List<String> sortParams) {
    return map(sortParams, getApprovalSearchRequestSortParams());
  }

  default List<BPSSortingQuery> map(List<String> sortParams, Map<String, String> bindings) {
    if (sortParams == null || sortParams.isEmpty()) {
      return new ArrayList<>();
    }

    return sortParams.stream()
        .map(e -> resolveParams(e, bindings))
        .filter(s -> nonNull(s.getSortOrderBy()))
        .collect(toList());
  }
}
