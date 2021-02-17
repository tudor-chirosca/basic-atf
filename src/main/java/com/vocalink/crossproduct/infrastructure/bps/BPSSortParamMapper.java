package com.vocalink.crossproduct.infrastructure.bps;

import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException;
import java.util.HashMap;
import java.util.Map;

public class BPSSortParamMapper {

  public static final String MINUS = "-";
  public static final String PLUS = "+";
  public static final String EMPTY = "";

  static Map<String, String> approvalSearchRequestSortParams;
  static {
    approvalSearchRequestSortParams = new HashMap<String, String>() {{
      put("participantName", "participantName");
      put("requestType", "requestType");
      put("jobId", "approvalId");
      put("createdAt", "date");
      put("requestedBy","requestedBy");
      put("status", "status");
    }};
  }

  public static BPSSortingQuery getApprovalSortParam(String sortParam) {
    BPSSortOrder order = sortParam.contains(MINUS) ? BPSSortOrder.DESC : BPSSortOrder.ASC;
    String bpsSort = approvalSearchRequestSortParams.get(sortParam
        .trim()
        .replace(MINUS, EMPTY)
        .replace(PLUS, EMPTY));
    if (bpsSort == null) {
      throw new NonConsistentDataException("Wrong sorting param: " + sortParam);
    }
    return new BPSSortingQuery(bpsSort, order);
  }
}
