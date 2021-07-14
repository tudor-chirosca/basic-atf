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
@JsonInclude(Include.NON_NULL)
public class SettlementDashboardRequest {

    private final String fundingParticipantId;

    public SettlementDashboardRequest(@JsonProperty("fundingParticipantId") final String fundingParticipantId) {
        this.fundingParticipantId = fundingParticipantId;
    }
}
