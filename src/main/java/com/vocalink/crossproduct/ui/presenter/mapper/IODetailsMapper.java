package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import java.time.LocalDate;
import org.modelmapper.ModelMapper;

public interface IODetailsMapper {

  static IODetailsDto map(IODetails ioDetails, Participant participant, LocalDate date) {
    final ModelMapper modelMapper = new ModelMapper();
    final ParticipantDto participantDto = modelMapper.map(participant, ParticipantDto.class);

    return modelMapper.typeMap(IODetails.class, IODetailsDto.class)
        .addMappings(mapper -> {
              mapper.map(IODetails::getBatches, IODetailsDto::setBatches);
              mapper.map(IODetails::getFiles, IODetailsDto::setFiles);
              mapper.map(IODetails::getTransactions, IODetailsDto::setTransactions);
            }
        ).setPostConverter(context -> {
          context.getDestination().setParticipant(participantDto);
          context.getDestination().setDateFrom(date);
          return context.getDestination();
        }).map(ioDetails);
  }
}
