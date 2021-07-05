package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

//Bindings used to match sort parameters, key for UI, value for BPS
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class MapperUtils {

  private static final Map<String, String> fileSearchRequestSortParams = new HashMap<>();
  static {
    fileSearchRequestSortParams.put("name", "fileName");
    fileSearchRequestSortParams.put("createdAt", "createdDate");
    fileSearchRequestSortParams.put("senderBic", "from");
    fileSearchRequestSortParams.put("receiverBic", "to");
    fileSearchRequestSortParams.put("messageType", "messageType");
    fileSearchRequestSortParams.put("nrOfBatches", "noOfBatches");
    fileSearchRequestSortParams.put("status", "status");
  }

  private static final Map<String, String> approvalSearchRequestSortParams = new HashMap<>();
  static {
    approvalSearchRequestSortParams.put("participants", "participantName");
    approvalSearchRequestSortParams.put("requestType", "requestType");
    approvalSearchRequestSortParams.put("jobId", "approvalId");
    approvalSearchRequestSortParams.put("createdAt", "date");
    approvalSearchRequestSortParams.put("requestedBy","requestedBy");
    approvalSearchRequestSortParams.put("status", "status");
  }

  private static final Map<String, String> batchSearchRequestSortParams = new HashMap<>();
  static {
    batchSearchRequestSortParams.put("id", "messageIdentifier");
    batchSearchRequestSortParams.put("createdAt", "createdDateTime");
    batchSearchRequestSortParams.put("senderBic", "instructingAgent");
    batchSearchRequestSortParams.put("receiverBic", "instructedAgent");
    batchSearchRequestSortParams.put("messageType","messageType");
    batchSearchRequestSortParams.put("nrOfTransactions","nrOfTransactions");
    batchSearchRequestSortParams.put("status", "status");
  }

  private static final Map<String, String> transactionSearchRequestSortParams = new HashMap<>();
  static {
    transactionSearchRequestSortParams.put("instructionId", "instructionId");
    transactionSearchRequestSortParams.put("createdAt", "createdDateTime");
    transactionSearchRequestSortParams.put("senderBic", "originator");
    transactionSearchRequestSortParams.put("messageType","messageType");
    transactionSearchRequestSortParams.put("amount", "amount");
    transactionSearchRequestSortParams.put("status", "status");
  }

  private static final Map<String, String> nameType = new HashMap<>();
  static {
    nameType.put("pacs.008", "Credit Transfer");
    nameType.put("pacs.004", "Payment Return or Positive Response to Recall of Payment");
    nameType.put("camt.056", "Cancellation / Recall of Credit Transfer");
    nameType.put("camt.029 v3", "Negative Response to Request for Recall");
    nameType.put("camt.029 v8", "Resolution of Investigation");
    nameType.put("camt.087", "Claim for Value Date Correction");
    nameType.put("camt.027", "Claim Non-Receipt");
  }

  private static final Map<String, String> settlementSearchRequestSortParams = new HashMap<>();
  static {
    settlementSearchRequestSortParams.put("cycleId", "cycleId");
    settlementSearchRequestSortParams.put("settlementTime", "settlementDate");
    settlementSearchRequestSortParams.put("status", "status");
    settlementSearchRequestSortParams.put("participantName", "participant");
  }

  private static final Map<String, String> settlementDetailsSearchRequestSortParams = new HashMap<>();
  static {
    settlementDetailsSearchRequestSortParams.put("reference", "settlementInstructionReference");
    settlementDetailsSearchRequestSortParams.put("status", "statusDetail");
    settlementDetailsSearchRequestSortParams.put("counterparty", "counterParty");
    settlementDetailsSearchRequestSortParams.put("settlementCounterparty", "counterPartySettlement");
    settlementDetailsSearchRequestSortParams.put("totalDebit", "totalAmountDebited");
    settlementDetailsSearchRequestSortParams.put("totalCredit", "totalAmountCredited");
  }

  private static final Map<String, String> managedParticipantSearchRequestSortParams = new HashMap<>();
  static {
    managedParticipantSearchRequestSortParams.put("name", "name");
    managedParticipantSearchRequestSortParams.put("status", "status");
    managedParticipantSearchRequestSortParams.put("organizationId", "partyExternalIdentifier");
    managedParticipantSearchRequestSortParams.put("participantType", "participantType");
    managedParticipantSearchRequestSortParams.put("tpspName", "tpspName");
    managedParticipantSearchRequestSortParams.put("fundedParticipantsCount", "fundedParticipantsCount");
  }

  private static final Map<String, String> reportsSearchRequestSortParams = new HashMap<>();
  static {
    reportsSearchRequestSortParams.put("reportId", "reportId");
    reportsSearchRequestSortParams.put("reportType", "reportType");
    reportsSearchRequestSortParams.put("createdAt", "reportDate");
    reportsSearchRequestSortParams.put("participantIdentifier", "partyCode");
    reportsSearchRequestSortParams.put("cycleId", "cycleId");
    reportsSearchRequestSortParams.put("participantName", "participantName");
  }

  private static final Map<String, String> messageDirections = new HashMap<>();
  static {
    messageDirections.put("sending", "input");
    messageDirections.put("receiving", "output");
  }

  static Map<String, String> getFileSearchRequestSortParams() {
    return unmodifiableMap(fileSearchRequestSortParams);
  }
  static Map<String, String> getApprovalSearchRequestSortParams() {
    return unmodifiableMap(approvalSearchRequestSortParams);
  }
  static Map<String, String> getBatchSearchRequestSortParams() {
    return unmodifiableMap(batchSearchRequestSortParams);
  }
  static Map<String, String> getTransactionSearchRequestSortParams() {
    return unmodifiableMap(transactionSearchRequestSortParams);
  }
  static Map<String, String> getSettlementSearchRequestSortParams() {
    return unmodifiableMap(settlementSearchRequestSortParams);
  }

  static Map<String, String> getSettlementDetailsSearchRequestSortParams() {
    return unmodifiableMap(settlementDetailsSearchRequestSortParams);
  }

  static Map<String, String> getManagedParticipantSearchRequestSortParams(){
    return unmodifiableMap(managedParticipantSearchRequestSortParams);
  }

  static Map<String, String> getReportsSearchRequestSortParams(){
    return unmodifiableMap(reportsSearchRequestSortParams);
  }

  static String getNameByType(String type) {
    return nameType.getOrDefault(type, "N/A");
  }

  static String getMessageDirection(String messageDirection) {
    return messageDirections.get(messageDirection);
  }
}
