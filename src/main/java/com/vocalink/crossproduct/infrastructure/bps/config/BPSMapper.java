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
  Cycle toEntity(BPSCycle cycle);

  @Mapping(target = "credit", source = "position", qualifiedByName = "countCredit")
  @Mapping(target = "debit", source = "position", qualifiedByName = "countDebit")
  @Mapping(target = "netPosition", source = "netPositionAmount.amount")
  ParticipantPosition toEntity(BPSSettlementPosition position);

  IntraDayPositionGross toEntity(BPSIntraDayPositionGross intraDay);

  @Mappings({
      @Mapping(target = "id", source = "schemeParticipantIdentifier"),
      @Mapping(target = "bic", source = "schemeParticipantIdentifier"),
      @Mapping(target = "name", source = "participantName"),
      @Mapping(target = "fundingBic", source = "participantConnectionId"),
      @Mapping(target = "suspendedTime", source = "effectiveTillDate"),
  })
  Participant toEntity(BPSParticipant bpsParticipant);

  @Mappings({
      @Mapping(target = "connectingParty", source = "connectingParty"),
      @Mapping(target = "participantType", source = "participantType")
  })
  BPSParticipantsSearchRequest toBps(String connectingParty, String participantType);

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
