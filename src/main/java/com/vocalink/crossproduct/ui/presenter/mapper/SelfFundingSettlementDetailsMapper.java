package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.Cycle;
import com.vocalink.crossproduct.domain.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.PositionDetails;
import com.vocalink.crossproduct.ui.dto.CycleDto;
import com.vocalink.crossproduct.ui.dto.IntraDayPositionGrossDto;
import com.vocalink.crossproduct.ui.dto.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.PositionDetailsTotalsDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

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
    CycleDto currentCycleDto = CycleMapper.map(cycles.get(1));
    CycleDto previousCycleDto = CycleMapper.map(cycles.get(0));

    PositionDetailsDto currentPositionDetailsDto = modelMapper
        .map(positionsDetails.get(1), PositionDetailsDto.class);
    PositionDetailsDto previousPositionDetailsDto = modelMapper
        .map(positionsDetails.get(0), PositionDetailsDto.class);

    PositionDetailsTotalsDto currentPositionDetailsTotalsDto = ParticipantPositionMapper
        .map(currentPositionDetailsDto);
    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = ParticipantPositionMapper
        .map(previousPositionDetailsDto);

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

    CycleDto previousCycleDto = CycleMapper.map(cycles.get(0));

    PositionDetailsDto previousPositionDetailsDto = modelMapper
        .map(positionsDetails.get(0), PositionDetailsDto.class);

    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = ParticipantPositionMapper
        .map(previousPositionDetailsDto);

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
