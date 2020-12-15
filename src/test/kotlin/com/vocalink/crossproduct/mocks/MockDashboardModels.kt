package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto

class MockDashboardModels {
    fun getSelfFundingDetailsDto(): ParticipantDashboardSettlementDetailsDto {
        return ParticipantDashboardSettlementDetailsDto.builder()
                .participant(MockParticipants().getParticipantDto(false))
                .previousCycle(MockCycles().cyclesDto[0])
                .currentCycle(MockCycles().cyclesDto[1])
                .currentPosition(MockPositions().positionDetailsDto)
                .previousPosition(MockPositions().positionDetailsDto)
                .previousPositionTotals(MockPositions().getPositionDetailsTotalsDto(true))
                .currentPositionTotals(MockPositions().getPositionDetailsTotalsDto(false))
                .build()
    }

    fun getSelfFundingDetailsDtoForOneCycle(): ParticipantDashboardSettlementDetailsDto {
        return ParticipantDashboardSettlementDetailsDto.builder()
                .participant(MockParticipants().getParticipantDto(false))
                .previousCycle(MockCycles().cyclesDto[0])
                .currentPosition(MockPositions().positionDetailsDto)
                .previousPosition(MockPositions().positionDetailsDto)
                .previousPositionTotals(MockPositions().getPositionDetailsTotalsDto(true))
                .build()
    }

    fun getAllParticipantsSettlementDashboardDto(): SettlementDashboardDto {
        return SettlementDashboardDto.builder()
                .previousCycle(MockCycles().cyclesDto[0])
                .currentCycle(MockCycles().cyclesDto[1])
                .positions(MockPositions().positionsDto)
                .build()
    }

    fun getFundingParticipantsSettlementDashboardDto(): SettlementDashboardDto {
        return SettlementDashboardDto.builder()
                .fundingParticipant(MockParticipants().fundingParticipantDto)
                .previousCycle(MockCycles().cyclesDto[0])
                .currentCycle(MockCycles().cyclesDto[1])
                .positions(MockPositions().positionsDto)
                .previousPositionTotals(MockPositions().getPositionDetailsTotalsDto(true))
                .currentPositionTotals(MockPositions().getPositionDetailsTotalsDto(false))
                .intraDayPositionTotals(MockPositions().getIntraDayPositionTotalDto(true))
                .build()
    }
}
