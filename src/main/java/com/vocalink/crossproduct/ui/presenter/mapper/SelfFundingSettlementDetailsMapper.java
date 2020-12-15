package com.vocalink.crossproduct.ui.presenter.mapper;

import static com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper.MAPPER;

import com.vocalink.crossproduct.domain.cycle.Cycle;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto;
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SelfFundingSettlementDetailsMapper {

  public ParticipantDashboardSettlementDetailsDto presentFullParticipantSettlementDetails(
      List<Cycle> cycles,
      List<PositionDetails> positionsDetails,
      Participant participant,
      Participant fundingParticipant,
      IntraDayPositionGross intradayPositionGross) {

    ParticipantDto participantDto = MAPPER.toDto(participant);

    CycleDto currentCycleDto = MAPPER.toDto(cycles.get(1));
    CycleDto previousCycleDto = MAPPER.toDto(cycles.get(0));

    PositionDetailsDto currentPositionDetailsDto = MAPPER.toDto(positionsDetails.get(1));
    PositionDetailsDto previousPositionDetailsDto = MAPPER.toDto(positionsDetails.get(0));

    PositionDetailsTotalsDto currentPositionDetailsTotalsDto = MAPPER.toDto(currentPositionDetailsDto);
    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = MAPPER.toDto(previousPositionDetailsDto);

    if (fundingParticipant == null || intradayPositionGross == null) {
      return ParticipantDashboardSettlementDetailsDto.builder()
          .participant(participantDto)
          .currentCycle(currentCycleDto)
          .previousCycle(previousCycleDto)
          .currentPosition(currentPositionDetailsDto)
          .previousPosition(previousPositionDetailsDto)
          .currentPositionTotals(currentPositionDetailsTotalsDto)
          .previousPositionTotals(previousPositionDetailsTotalsDto)
          .build();
    }

    return ParticipantDashboardSettlementDetailsDto.builder()
        .participant(participantDto)
        .settlementBank(MAPPER.toDto(fundingParticipant))
        .currentCycle(currentCycleDto)
        .previousCycle(previousCycleDto)
        .currentPosition(currentPositionDetailsDto)
        .previousPosition(previousPositionDetailsDto)
        .currentPositionTotals(currentPositionDetailsTotalsDto)
        .previousPositionTotals(previousPositionDetailsTotalsDto)
        .intraDayPositionGross(MAPPER.toDto(intradayPositionGross))
        .build();
  }

  public ParticipantDashboardSettlementDetailsDto presentOneCycleParticipantSettlementDetails(
      List<Cycle> cycles, List<PositionDetails> positionsDetails, Participant participant,
      Participant fundingParticipant, IntraDayPositionGross intradayPositionGross) {

    ParticipantDto participantDto = MAPPER.toDto(participant);

    CycleDto previousCycleDto = MAPPER.toDto(cycles.get(0));

    PositionDetailsDto previousPositionDetailsDto = MAPPER.toDto(positionsDetails.get(0));

    PositionDetailsTotalsDto previousPositionDetailsTotalsDto = MAPPER.toDto(previousPositionDetailsDto);

    if (fundingParticipant == null || intradayPositionGross == null) {
      return ParticipantDashboardSettlementDetailsDto.builder()
          .participant(participantDto)
          .previousCycle(previousCycleDto)
          .previousPosition(previousPositionDetailsDto)
          .previousPositionTotals(previousPositionDetailsTotalsDto)
          .build();
    }

    return ParticipantDashboardSettlementDetailsDto.builder()
        .participant(participantDto)
        .settlementBank(MAPPER.toDto(fundingParticipant))
        .previousCycle(previousCycleDto)
        .previousPosition(previousPositionDetailsDto)
        .previousPositionTotals(previousPositionDetailsTotalsDto)
        .intraDayPositionGross(MAPPER.toDto(intradayPositionGross))
        .build();
  }
}
