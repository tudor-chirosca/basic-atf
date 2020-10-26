package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import java.math.BigInteger;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper MAPPER = Mappers.getMapper(DTOMapper.class);

  CycleDto toDto(Cycle cycle);

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
}
