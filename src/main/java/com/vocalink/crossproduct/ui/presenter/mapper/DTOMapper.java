package com.vocalink.crossproduct.ui.presenter.mapper;

import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper MAPPER = Mappers.getMapper(DTOMapper.class);

  CycleDto toDto(Cycle cycle);

  AlertReferenceDataDto toDto(AlertReferenceData alertReferenceData);

  AlertStatsDto toDto(AlertStats alertStats);

  ParticipantPositionDto toDto(ParticipantPosition participant);

  @Mapping(target = "dateFrom", source = "date")
  IODetailsDto toDto(IODetails ioDetails, Participant participant, LocalDate date);

  @Mapping(target = "totalCredit", source = "details", qualifiedByName = "countCredit")
  @Mapping(target = "totalDebit", source = "details", qualifiedByName = "countDebit")
  @Mapping(target = "totalNetPosition", source = "details", qualifiedByName = "countNetPosition")
  PositionDetailsTotalsDto toDto(PositionDetailsDto details);

  ParticipantDto toDto(Participant participant);

  PositionDetailsDto toDto(PositionDetails positionDetails);

  IntraDayPositionGrossDto toDto(IntraDayPositionGross intraDayPositionGross);

  @Named("countCredit")
  default BigInteger countCredit(PositionDetailsDto details) {
    return details.getCustomerCreditTransfer().getCredit()
        .add(details.getPaymentReturn().getCredit());
  }

  @Named("countDebit")
  default BigInteger countDebit(PositionDetailsDto details) {
    return details.getCustomerCreditTransfer().getDebit()
        .add(details.getPaymentReturn().getDebit());
  }

  @Named("countNetPosition")
  default BigInteger countNetPosition(PositionDetailsDto details) {
    return details.getCustomerCreditTransfer().getNetPosition()
        .add(details.getPaymentReturn().getNetPosition());
  }

  @Mapping(target = "currentPosition", source = "currentCycle.totalPositions", qualifiedByName = "generatePosition")
  @Mapping(target = "previousPosition", source = "previousCycle.totalPositions", qualifiedByName = "generatePosition")
  TotalPositionDto toDto(Participant participant, Cycle currentCycle, Cycle previousCycle,
      @Context String participantID);

  @Mapping(target = "currentPosition", source = "currentCycle.totalPositions", qualifiedByName = "generatePosition")
  @Mapping(target = "previousPosition", source = "previousCycle.totalPositions", qualifiedByName = "generatePosition")
  @Mapping(target = "intraDayPositionGross", source = "intraDays", qualifiedByName = "generateIntraDays")
  TotalPositionDto toDto(Participant participant, Cycle currentCycle, Cycle previousCycle,
      List<IntraDayPositionGross> intraDays, @Context String participantID);

  @Named("generatePosition")
  default ParticipantPositionDto generatePosition(List<ParticipantPosition> positions, @Context String participantId) {
    if (positions == null) {
      return ParticipantPositionDto.builder().build();
    }
    return positions.stream()
        .filter(pos -> pos.getParticipantId().equals(participantId))
        .findFirst()
        .map(MAPPER::toDto)
        .orElse(ParticipantPositionDto.builder().build());
  }

  @Named("generateIntraDays")
  default IntraDayPositionGrossDto generateIntraDays(List<IntraDayPositionGross> intraDays,
      @Context String participantId) {
    if (intraDays == null) {
      return IntraDayPositionGrossDto.builder().build();
    }
    return intraDays.stream()
        .filter(pos -> pos.getParticipantId().equals(participantId))
        .findFirst()
        .map(MAPPER::toDto)
        .orElse(IntraDayPositionGrossDto.builder().build());
  }

  @Mapping(target = "currentCycle", source = "currentCycle")
  @Mapping(target = "previousCycle", source = "previousCycle")
  SettlementDashboardDto toDto(Cycle currentCycle, Cycle previousCycle,
      List<TotalPositionDto> positions);

  @Mapping(target = "currentCycle", source = "currentCycle")
  @Mapping(target = "previousCycle", source = "previousCycle")
  @Mapping(target = "positions", source = "positions")
  @Mapping(target = "currentPositionTotals", source = "positions", qualifiedByName = "countCurrentPositionTotals")
  @Mapping(target = "previousPositionTotals", source = "positions", qualifiedByName = "countPreviousPositionTotals")
  @Mapping(target = "intraDayPositionTotals", source = "intraDays", qualifiedByName = "countIntraDayTotals")
  SettlementDashboardDto toDto(Cycle currentCycle, Cycle previousCycle,
      List<TotalPositionDto> positions, Participant fundingParticipant,
      List<IntraDayPositionGross> intraDays);

  @Named("countCurrentPositionTotals")
  default PositionDetailsTotalsDto countCurrentPositionTotals(List<TotalPositionDto> positions) {
      return countPositionTotals(positions.stream()
          .map(TotalPositionDto::getCurrentPosition)
          .collect(toList()));
  }

  @Named("countPreviousPositionTotals")
  default PositionDetailsTotalsDto countPreviousPositionTotals(List<TotalPositionDto> positions) {
    return countPositionTotals(positions.stream()
        .map(TotalPositionDto::getPreviousPosition)
        .collect(toList()));
  }

  default PositionDetailsTotalsDto countPositionTotals(List<ParticipantPositionDto> positions) {
    return PositionDetailsTotalsDto.builder()
        .totalCredit(positions.stream()
            .map(ParticipantPositionDto::getCredit)
            .filter(Objects::nonNull)
            .reduce(BigInteger::add).orElse(BigInteger.ZERO))
        .totalDebit(positions.stream()
            .map(ParticipantPositionDto::getDebit)
            .filter(Objects::nonNull)
            .reduce(BigInteger::add).orElse(BigInteger.ZERO))
        .build();
  }

  @Named("countIntraDayTotals")
  default IntraDayPositionTotalDto countIntraDayTotals(List<IntraDayPositionGross> intraDays) {
    if (intraDays == null) {
      return IntraDayPositionTotalDto.builder().build();
    }
    return IntraDayPositionTotalDto.builder()
        .totalDebitCap(intraDays.stream()
            .map(IntraDayPositionGross::getDebitCap)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
        .totalDebitPosition(intraDays.stream()
            .map(IntraDayPositionGross::getDebitPosition)
            .filter(Objects::nonNull)
            .reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
        .build();
  }
}
