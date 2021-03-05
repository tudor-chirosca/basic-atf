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
    put("participantName", "participantName");
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
}
