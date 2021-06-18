package com.vocalink.crossproduct.ui.dto.settlement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SettlementDashboardRequest {

    @JsonInclude(Include.NON_NULL)
    private final String fundingParticipantId;

    public SettlementDashboardRequest(@JsonProperty("fundingParticipantId") final String fundingParticipantId) {
        this.fundingParticipantId = fundingParticipantId;
    }
}
