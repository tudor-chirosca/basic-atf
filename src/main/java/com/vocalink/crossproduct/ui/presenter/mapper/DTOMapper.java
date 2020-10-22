package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper MAPPER = Mappers.getMapper(DTOMapper.class);

  CycleDto toDto(Cycle cycle);

  @Mapping(target = "dateFrom", source = "date")
  IODetailsDto toDto(IODetails ioDetails, Participant participant, LocalDate date);

  default PositionDetailsTotalsDto toDto(PositionDetailsDto details) {

    return PositionDetailsTotalsDto.builder()
        .totalCredit(details.getCustomerCreditTransfer().getCredit().add(
            details.getPaymentReturn().getCredit()))
        .totalDebit(details.getCustomerCreditTransfer().getDebit().add(
            details.getPaymentReturn().getDebit()))
        .totalNetPosition(details.getCustomerCreditTransfer().getNetPosition().add(
            details.getPaymentReturn().getNetPosition()))
        .build();
  }
}
