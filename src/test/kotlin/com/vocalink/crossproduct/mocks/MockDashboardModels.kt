package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto

class MockDashboardModels {
    fun getSelfFundingDetailsDto(): ParticipantSettlementDetailsDto {
        return ParticipantSettlementDetailsDto.builder()
                .participant(MockParticipants().getParticipantDto(false))
                .previousCycle(MockCycles().cyclesDto[0])
                .currentCycle(MockCycles().cyclesDto[1])
                .currentPosition(MockPositions().positionDetailsDto)
                .previousPosition(MockPositions().positionDetailsDto)
                .previousPositionTotals(MockPositions().getPositionDetailsTotalsDto(true))
                .currentPositionTotals(MockPositions().getPositionDetailsTotalsDto(false))
                .build()
    }

    fun getSelfFundingDetailsDtoForOneCycle(): ParticipantSettlementDetailsDto {
        return ParticipantSettlementDetailsDto.builder()
                .participant(MockParticipants().getParticipantDto(false))
                .previousCycle(MockCycles().cyclesDto[0])
                .currentPosition(MockPositions().positionDetailsDto)
                .previousPosition(MockPositions().positionDetailsDto)
                .previousPositionTotals(MockPositions().getPositionDetailsTotalsDto(true))
                .build()
    }

    fun getSettlementDashboardDto(): SettlementDashboardDto {
        return SettlementDashboardDto.builder()
                .previousCycle(MockCycles().cyclesDto[0])
                .currentCycle(MockCycles().cyclesDto[1])
                .positions(MockPositions().positionsDto)
                .build()
    }
}