package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.ui.dto.SelfFundingSettlementDetailsDto
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto

class MockDashboardModels {
    fun getSelfFundingDetailsDto(): SelfFundingSettlementDetailsDto {
        return SelfFundingSettlementDetailsDto.builder()
                .participant(MockParticipants().getParticipantDto(false))
                .previousCycle(MockCycles().cyclesDto[0])
                .currentCycle(MockCycles().cyclesDto[1])
                .customerCreditTransfer(MockPositions().positionDetailsDto)
                .paymentReturn(MockPositions().positionDetailsDto)
                .previousPositionTotals(MockPositions().getPositionDetailsTotalsDto(true))
                .currentPositionTotals(MockPositions().getPositionDetailsTotalsDto(false))
                .build()
    }

    fun getSelfFundingDetailsDtoForOneCycle(): SelfFundingSettlementDetailsDto {
        return SelfFundingSettlementDetailsDto.builder()
                .participant(MockParticipants().getParticipantDto(false))
                .previousCycle(MockCycles().cyclesDto[0])
                .customerCreditTransfer(MockPositions().positionDetailsDto)
                .paymentReturn(MockPositions().positionDetailsDto)
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