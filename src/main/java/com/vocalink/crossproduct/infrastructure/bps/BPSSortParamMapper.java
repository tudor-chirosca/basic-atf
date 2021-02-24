package com.vocalink.crossproduct.infrastructure.bps;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BPSSortParamMapper {

  public static final String MINUS = "-";
  public static final String PLUS = "+";
  public static final String EMPTY = "";

  public static BPSSortingQuery resolveParams(String sortParam, Map<String, String> bindings) {
    BPSSortOrder order = sortParam.contains(MINUS) ? BPSSortOrder.DESC : BPSSortOrder.ASC;
    String bpsSort = bindings.get(sortParam
        .trim()
        .replace(MINUS, EMPTY)
        .replace(PLUS, EMPTY));

    return new BPSSortingQuery(bpsSort, order);
  }
}
