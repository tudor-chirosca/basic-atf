package com.vocalink.crossproduct.infrastructure.bps.report;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class BPSReportSearchParticipant {

    private final String schemeCode;
    private final String schemeParticipantIdentifier;
    private final String countryCode;
    private final String partyCode;
    private final String connectingParty;
    private final ParticipantType participantType;
    private final ParticipantStatus status;
    private final ZonedDateTime effectiveFromDate;
    private final ZonedDateTime effectiveTillDate;
    private final String participantName;
    private final String rcvngParticipantConnectionId;
    private final String participantConnectionId;
    private final String partyExternalIdentifier;
}
