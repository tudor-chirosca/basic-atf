package com.vocalink.crossproduct.ui.dto.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.participant.SuspensionLevel;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ManagedParticipantDetailsDto {

  private final String bic;
  private final String fundingBic;
  private final String id;
  private final String name;
  private final ParticipantStatus status;
  @JsonInclude(Include.NON_EMPTY)
  private final ZonedDateTime suspendedTime;
  private final ParticipantType participantType;
  private final String organizationId;
  private final Boolean hasActiveSuspensionRequests;
  @JsonInclude(Include.NON_EMPTY)
  private final SuspensionLevel suspensionLevel;
  @JsonInclude(Include.NON_EMPTY)
  private final String tpspName;
  @JsonInclude(Include.NON_EMPTY)
  private final String tpspId;
  @JsonInclude(Include.NON_EMPTY)
  private final ParticipantReferenceDto fundingParticipant;
  @JsonInclude(Include.NON_EMPTY)
  private final List<ParticipantReferenceDto> fundedParticipants;
  private final String outputChannel;
  private final Integer outputTxnVolume;
  @JsonInclude(Include.NON_EMPTY)
  private final Integer outputTxnTimeLimit;
  @JsonInclude(Include.NON_EMPTY)
  private final BigDecimal debitCapLimit;
  @JsonInclude(Include.NON_EMPTY)
  private final List<Double> debitCapLimitThresholds;
  @JsonInclude(Include.NON_EMPTY)
  private final String settlementAccountNo;
  @JsonInclude(Include.NON_EMPTY)
  private final ZonedDateTime updatedAt;
  @JsonInclude(Include.NON_EMPTY)
  private final ApprovalUserDto updatedBy;
  private final ApprovalReferenceDto approvalReference;

  public String getParticipantType() {
    return participantType.getDescription();
  }
}