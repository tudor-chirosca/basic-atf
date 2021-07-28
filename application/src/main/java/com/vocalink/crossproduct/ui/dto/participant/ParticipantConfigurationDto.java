package com.vocalink.crossproduct.ui.dto.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.participant.OutputFlow;
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
@JsonInclude(Include.NON_EMPTY)
public class ParticipantConfigurationDto {

  private final String bic;
  private final String fundingBic;
  private final String id;
  private final String name;
  private final ParticipantStatus status;
  private final ZonedDateTime suspendedTime;
  private final ParticipantType participantType;
  private final SuspensionLevel suspensionLevel;
  private final String organizationId;
  private final Boolean hasActiveSuspensionRequests;
  private final String tpspName;
  private final String tpspId;
  private final ParticipantReferenceDto fundingParticipant;
  private final String outputChannel;
  private final List<OutputFlow> outputFlow;
  private final BigDecimal debitCapLimit;
  private final List<Double> debitCapLimitThresholds;
  private final String settlementAccountNo;
  private final ZonedDateTime updatedAt;
  private final ApprovalUserDto updatedBy;
  private final ApprovalReferenceDto approvalReference;

  public String getParticipantType() {
    return participantType.getDescription();
  }
}