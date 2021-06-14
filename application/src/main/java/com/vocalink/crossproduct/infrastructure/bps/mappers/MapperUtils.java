package com.vocalink.crossproduct.infrastructure.bps.mappers;

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

//Bindings used to match sort parameters, key for UI, value for BPS
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
abstract class MapperUtils {

  private static final Map<String, String> fileSearchRequestSortParams = new HashMap<String, String>() {{
    put("name", "fileName");
    put("createdAt", "createdDate");
    put("senderBic", "originator");
    put("messageType", "messageType");
    put("nrOfBatches", "nrOfBatches");
    put("status", "status");
  }};

  private static final Map<String, String> approvalSearchRequestSortParams = new HashMap<String, String>() {{
    put("participants", "participantName");
    put("requestType", "requestType");
    put("jobId", "approvalId");
    put("createdAt", "date");
    put("requestedBy","requestedBy");
    put("status", "status");
  }};

  private static final Map<String, String> batchSearchRequestSortParams = new HashMap<String, String>() {{
    put("id", "messageIdentifier");
    put("createdAt", "createdDateTime");
    put("senderBic", "originator");
    put("messageType","messageType");
    put("nrOfTransactions","nrOfTransactions");
    put("status", "status");
  }};

  private static final Map<String, String> transactionSearchRequestSortParams = new HashMap<String, String>() {{
    put("instructionId", "instructionId");
    put("createdAt", "createdDateTime");
    put("senderBic", "originator");
    put("messageType","messageType");
    put("amount", "amount");
    put("status", "status");
  }};

  private static final Map<String, String> nameType = new HashMap<String, String>() {{
    put("pacs.008", "Credit Transfer");
    put("pacs.004", "Payment Return or Positive Response to Recall of Payment");
    put("camt.056", "Cancellation / Recall of Credit Transfer");
    put("camt.029 v3", "Negative Response to Request for Recall");
    put("camt.029 v8", "Resolution of Investigation");
    put("camt.087", "Claim for Value Date Correction");
    put("camt.027", "Claim Non-Receipt");
  }};

  private static final Map<String, String> settlementSearchRequestSortParams = new HashMap<String, String>() {{
    put("cycleId", "cycleId");
    put("settlementTime", "settlementDate");
    put("status", "status");
    put("participantName", "participant");
  }};

  private static final Map<String, String> settlementDetailsSearchRequestSortParams = new HashMap<String, String>() {{
    put("reference", "settlementInstructionReference");
    put("status", "statusDetail");
    put("counterparty", "counterParty");
    put("settlementCounterparty", "counterPartySettlement");
    put("totalDebit", "totalAmountDebited");
    put("totalCredit", "totalAmountCredited");
  }};

  private static final Map<String, String> managedParticipantSearchRequestSortParams = new HashMap<String, String>() {{
    put("name", "name");
    put("status", "status");
    put("organizationId", "partyExternalIdentifier");
    put("participantType", "participantType");
    put("tpspName", "tpspName");
    put("fundedParticipantsCount", "fundedParticipantsCount");
  }};

  private static final Map<String, String> messageType = new HashMap<String, String>() {{
    put("input", "sending");
    put("output", "receiving");
    put("input / output", "sending / receiving");
  }};

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

  static String getNameByType(String type) {
    return nameType.getOrDefault(type, "N/A");
  }

  static String getMessageType(String type) {
    return messageType.get(type);
  }

}
