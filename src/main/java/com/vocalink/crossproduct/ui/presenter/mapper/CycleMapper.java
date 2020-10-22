package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CycleMapper {

  CycleMapper CYCLE_MAPPER = Mappers.getMapper(CycleMapper.class);

  CycleDto toDto(Cycle cycle);
}