package com.vocalink.crossproduct.infrastructure.bps.config;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycle;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSSettlementPosition;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipantsSearchRequest;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntraDayPositionGross;
import java.math.BigDecimal;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BPSMapper {

  BPSMapper BPSMAPPER = Mappers.getMapper(BPSMapper.class);

  @Mapping(target = "id", source = "cycleId")
  @Mapping(target = "cutOffTime", source = "fileSubmissionCutOffTime")
  Cycle toCp(BPSCycle cycle);

  @Mapping(target = "credit", source = "position", qualifiedByName = "countCredit")
  @Mapping(target = "debit", source = "position", qualifiedByName = "countDebit")
  @Mapping(target = "netPosition", source = "netPositionAmount.amount")
  ParticipantPosition toCp(BPSSettlementPosition position);

  IntraDayPositionGross toCp(BPSIntraDayPositionGross intraDay);

  BPSParticipantsSearchRequest toBps(Map<String, Object> participantSearchCriteria);

  @Mappings({
      @Mapping(target = "id", source = "schemeParticipantIdentifier"),
      @Mapping(target = "bic", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "fundingBic", source = "participantConnectionId"),
      @Mapping(target = "suspendedTime", source = "effectiveTillDate"),
  })
  Participant toCp(BPSParticipant stats);

  @Named("castParticipantSettlementStatus")
  default SettlementStatus castParticipantSettlementStatus(String status) {
    return SettlementStatus.valueOf(status.replaceAll("[_+-]", "_").toUpperCase());
  }

  CPPage<CPParticipantSettlement> toCp(BPSPage<BPSParticipantSettlement> settlement);

  @Mapping(target = "cycleId", source = "cycleId")
  @Mapping(target = "participantId", source = "participantId")
  BPSInstructionEnquiryRequest toBps(CPInstructionEnquiryRequest request, String cycleId,
      String participantId);

  @Mapping(target = "cycleId", source = "cycleId")
  @Mapping(target = "participantId", source = "participantId")
  BPSParticipantSettlementRequest toBps(String cycleId, String participantId);

  CPPage<CPAlert> toCpAlert(BPSPage<BPSAlert> alert);

  @Deprecated
  BPSAlertsRequest toBps(CPAlertRequest request);

  BPSAlertsSearchParam toBps(CPAlertParams params);



  CPAlert toCp(BPSAlert reference);

  CPAlertStats toCp(BPSAlertStats stats);

  CPAlertReferenceData toCp(BPSAlertReferenceData referenceData);



  CPMessageDirectionReference toCp(BPSMessageDirectionReference reference);



  CPParticipantIOData toCp(BPSParticipantIOData participantIOData);

  CPIOData toCp(BPSIOData ioData);

  CPIODetails toCp(BPSIODetails ioDetails);

  CPIODataDetails toCp(BPSIODataDetails ioDataDetails);

  CPIODataAmountDetails toCp(BPSIODataAmountDetails ioDataDetails);

  CPIOBatchesMessageTypes toCp(BPSIOBatchesMessageTypes ioBatchMessageType);

  CPIOTransactionsMessageTypes toCp(BPSIOTransactionsMessageTypes ioTransactionMessageType);


  @Named("countCredit")
  default BigDecimal countCredit(BPSSettlementPosition position) {
    return position.getPaymentReceived().getAmount().getAmount()
        .add(position.getReturnReceived().getAmount().getAmount());
  }

  @Named("countDebit")
  default BigDecimal countDebit(BPSSettlementPosition position) {
    return position.getPaymentSent().getAmount().getAmount()
        .add(position.getReturnSent().getAmount().getAmount());
  }
}
