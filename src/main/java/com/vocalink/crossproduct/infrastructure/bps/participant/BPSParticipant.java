package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.vocalink.crossproduct.shared.participant.ParticipantType;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class BPSParticipant {

  private String schemeCode;
  private String schemeParticipantIdentifier;
  private String countryCode;
  private String partyCode;
  private ParticipantType participantType;
  private String connectingParty;
  private String status;
  private ZonedDateTime effectiveFromDate;
  private ZonedDateTime effectiveTillDate;
  private String participantName;
  private String rcvngParticipantConnectionId;
  private String participantConnectionId;

  public void setParticipantType(String participantType) {
    this.participantType = ParticipantType.valueOf(participantType.replaceAll("[_+-]", "_"));
  }
}
