package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.ui.dto.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.PositionDetailsTotalsDto;
import org.modelmapper.ModelMapper;

public interface ParticipantPositionMapper {

  static PositionDetailsTotalsDto map(PositionDetailsDto positionDetailsDto) {

    final ModelMapper modelMapper = new ModelMapper();

    return modelMapper.typeMap(PositionDetailsDto.class, PositionDetailsTotalsDto.class)
        .setPostConverter(context -> {
          context.getDestination()
              .setTotalCredit(context.getSource().getCustomerCreditTransfer().getCredit()
                  .add(context.getSource().getPaymentReturn().getCredit()));
          context.getDestination()
              .setTotalDebit(context.getSource().getCustomerCreditTransfer().getDebit()
                  .add(context.getSource().getPaymentReturn().getDebit()));
          context.getDestination()
              .setTotalNetPosition(context.getSource().getCustomerCreditTransfer().getNetPosition()
                  .add(context.getSource().getPaymentReturn().getNetPosition()));
          return context.getDestination();
        }).map(positionDetailsDto);
  }
}