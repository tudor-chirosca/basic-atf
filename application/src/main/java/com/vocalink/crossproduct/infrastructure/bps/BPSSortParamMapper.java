package com.vocalink.crossproduct.infrastructure.bps;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import static com.vocalink.crossproduct.infrastructure.bps.BPSSortOrder.ASC;
import static com.vocalink.crossproduct.infrastructure.bps.BPSSortOrder.DESC;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BPSSortParamMapper {

    private static final String MINUS = "-";
    private static final String PLUS = "+";
    public static final BPSSortOrder DEFAULT_SORTING_ORDER = ASC;

    public static BPSSortingQuery resolveParams(String sortParam, Map<String, String> bindings) {
        String param = StringUtils.trim(sortParam);
        if (StringUtils.startsWith(param, MINUS)) {
            return new BPSSortingQuery(bindings.get(StringUtils.substring(param, 1)), DESC);
        }
        if (StringUtils.startsWith(param, PLUS)) {
            return new BPSSortingQuery(bindings.get(StringUtils.substring(param, 1)), ASC);
        }
        return new BPSSortingQuery(bindings.get(param), DEFAULT_SORTING_ORDER);
    }
}
