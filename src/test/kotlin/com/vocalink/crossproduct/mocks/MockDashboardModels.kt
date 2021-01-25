package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto
import java.math.BigDecimal

class MockDashboardModels {

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
                .previousPositionTotals(PositionDetailsTotalsDto(BigDecimal.TEN, BigDecimal.TEN))
                .currentPositionTotals(PositionDetailsTotalsDto(BigDecimal.ZERO, BigDecimal.TEN))
                .intraDayPositionTotals(MockPositions().getIntraDayPositionTotalDto(true))
                .build()
    }
}
