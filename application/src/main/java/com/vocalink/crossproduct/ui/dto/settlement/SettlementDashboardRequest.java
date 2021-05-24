package com.vocalink.crossproduct.ui.dto.settlement;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SettlementDashboardRequest {

    @JsonInclude(NON_NULL)
    private final String fundingParticipantId;

}
