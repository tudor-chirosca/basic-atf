package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.ui.dto.CycleDto;
import org.modelmapper.ModelMapper;

public interface CycleMapper {

  static CycleDto map(Cycle cycle) {
    final ModelMapper modelMapper = new ModelMapper();

    return modelMapper.typeMap(Cycle.class, CycleDto.class)
        .addMappings(mapper -> {
              mapper.map(Cycle::getId, CycleDto::setId);
              mapper.map(Cycle::getCutOffTime, CycleDto::setCutOffTime);
              mapper.map(Cycle::getSettlementTime, CycleDto::setId);
              mapper.map(Cycle::getId, CycleDto::setId);
            }
        ).map(cycle);
  }
}