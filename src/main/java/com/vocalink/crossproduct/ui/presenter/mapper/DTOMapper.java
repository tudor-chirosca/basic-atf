package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionTotalDto;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper MAPPER = Mappers.getMapper(DTOMapper.class);

  CycleDto toDto(Cycle cycle);

  ParticipantDto toDto(Participant participant);

  ParticipantPositionDto toDto(ParticipantPosition participant);

  @Mapping(target = "dateFrom", source = "date")
  IODetailsDto toDto(IODetails ioDetails, Participant participant, LocalDate date);

  @Mapping(target = "totalCredit", source = "details", qualifiedByName = "countCredit")
  @Mapping(target = "totalDebit", source = "details", qualifiedByName = "countDebit")
  @Mapping(target = "totalNetPosition", source = "details", qualifiedByName = "countNetPosition")
  PositionDetailsTotalsDto toDto(PositionDetailsDto details);

  PositionDetailsDto toDto(PositionDetails positionDetails);

  IntraDayPositionGrossDto toDto(IntraDayPositionGross intraDayPositionGross);

  @Named("countCredit")
  static BigInteger countCredit(PositionDetailsDto details) {
    return details.getCustomerCreditTransfer().getCredit()
        .add(details.getPaymentReturn().getCredit());
  }

  @Named("countDebit")
  static BigInteger countDebit(PositionDetailsDto details) {
    return details.getCustomerCreditTransfer().getDebit()
        .add(details.getPaymentReturn().getDebit());
  }

  @Named("countNetPosition")
  static BigInteger countNetPosition(PositionDetailsDto details) {
    return details.getCustomerCreditTransfer().getNetPosition()
        .add(details.getPaymentReturn().getNetPosition());
  }

  default IntraDayPositionTotalDto toDto(List<IntraDayPositionGross> intraDays) {
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
