package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IODetailsMapper {

  IODetailsMapper IO_DETAILS_MAPPER = Mappers.getMapper(IODetailsMapper.class);

  @Mapping(target = "dateFrom", source = "date")
  IODetailsDto toDto(IODetails ioDetails, Participant participant, LocalDate date);
}
