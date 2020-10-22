package com.vocalink.crossproduct.ui.presenter.mapper;

import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SelfFundingSettlementDetailsMapper {

  final private ModelMapper modelMapper = new ModelMapper();

  public ParticipantSettlementDetailsDto presentFullParticipantSettlementDetails(
      List<Cycle> cycles,
      List<PositionDetails> positionsDetails,
      Participant participant,
      Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross) {

    ParticipantDto participantDto = modelMapper.map(participant, ParticipantDto.class);
    CycleDto currentCycleDto = MAPPER.toDto(cycles.get(1));
    CycleDto previousCycleDto = MAPPER.toDto(cycles.get(0));

    PositionDetailsDto currentPositionDetailsDto = modelMapper
        .map(positionsDetails.get(1), PositionDetailsDto.class);
    PositionDetailsDto previousPositionDetailsDto = modelMapper
        .map(positionsDetails.get(0), PositionDetailsDto.class);

    PositionDetailsTotalsDto currentPositionDetailsTotalsDto = MAPPER.toDto(currentPositionDetailsDto);
    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = MAPPER.toDto(previousPositionDetailsDto);

    if (fundingParticipant != null && intradayPositionGross != null) {
      ParticipantDto fundingParticipantDto = modelMapper.map(fundingParticipant, ParticipantDto.class);
      IntraDayPositionGrossDto intradayPositionGrossDto = modelMapper
          .map(intradayPositionGross, IntraDayPositionGrossDto.class);

      return ParticipantSettlementDetailsDto.builder()
          .participant(participantDto)
          .settlementBank(fundingParticipantDto)
          .currentCycle(currentCycleDto)
          .previousCycle(previousCycleDto)
          .currentPosition(currentPositionDetailsDto)
          .previousPosition(previousPositionDetailsDto)
          .currentPositionTotals(currentPositionDetailsTotalsDto)
          .previousPositionTotals(previousPositionDetailsTotalsDto)
          .intraDayPositionGross(intradayPositionGrossDto)
          .build();
    }

    return ParticipantSettlementDetailsDto.builder()
        .participant(participantDto)
        .currentCycle(currentCycleDto)
        .previousCycle(previousCycleDto)
        .currentPosition(currentPositionDetailsDto)
        .previousPosition(previousPositionDetailsDto)
        .currentPositionTotals(currentPositionDetailsTotalsDto)
        .previousPositionTotals(previousPositionDetailsTotalsDto)
        .build();
  }

  public ParticipantSettlementDetailsDto presentOneCycleParticipantSettlementDetails(
      List<Cycle> cycles, List<PositionDetails> positionsDetails, Participant participant,
      Participant fundingParticipant, IntraDayPositionGross intradayPositionGross) {

    ParticipantDto participantDto = modelMapper.map(participant, ParticipantDto.class);

    CycleDto previousCycleDto = MAPPER.toDto(cycles.get(0));

    PositionDetailsDto previousPositionDetailsDto = modelMapper
        .map(positionsDetails.get(0), PositionDetailsDto.class);

    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = MAPPER.toDto(previousPositionDetailsDto);

    if (fundingParticipant != null && intradayPositionGross != null) {
      ParticipantDto fundingParticipantDto = modelMapper.map(fundingParticipant, ParticipantDto.class);
      IntraDayPositionGrossDto intradayPositionGrossDto = modelMapper
          .map(intradayPositionGross, IntraDayPositionGrossDto.class);

      return ParticipantSettlementDetailsDto.builder()
          .participant(participantDto)
          .settlementBank(fundingParticipantDto)
          .previousCycle(previousCycleDto)
          .previousPosition(previousPositionDetailsDto)
          .previousPositionTotals(previousPositionDetailsTotalsDto)
          .intraDayPositionGross(intradayPositionGrossDto)
          .build();
    }

    return ParticipantSettlementDetailsDto.builder()
        .participant(participantDto)
        .previousCycle(previousCycleDto)
        .previousPosition(previousPositionDetailsDto)
        .previousPositionTotals(previousPositionDetailsTotalsDto)
        .build();
  }
}
