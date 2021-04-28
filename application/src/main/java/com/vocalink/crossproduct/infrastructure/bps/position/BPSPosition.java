package com.vocalink.crossproduct.infrastructure.bps.position;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class BPSPosition {

    private final BigDecimal credit;
    private final BigDecimal debit;
    private final BigDecimal netPosition;

    public BPSPosition(
         @JsonProperty(value = "credit") BigDecimal credit,
         @JsonProperty(value = "debit") BigDecimal debit,
         @JsonProperty(value = "netPosition") BigDecimal netPosition) {
        this.credit = credit;
        this.debit = debit;
        this.netPosition = netPosition;
    }
}
