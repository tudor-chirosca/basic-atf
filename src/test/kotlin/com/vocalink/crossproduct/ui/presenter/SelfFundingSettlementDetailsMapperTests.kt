package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.mocks.MockCycles
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.mocks.MockPositions
import com.vocalink.crossproduct.shared.participant.ParticipantStatus
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.jupiter.api.Test

class SelfFundingSettlementDetailsMapperTests {

    private val selfFundingSettlementDetailsMapper = SelfFundingSettlementDetailsMapper()

    @Test
    fun `should get Self Funding Settlement Details DTO for 2 cycles`() {
        val cycles = MockCycles().cycles
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(false)

        val result = selfFundingSettlementDetailsMapper.presentFullParticipantSettlementDetails(cycles, positionDetails, participant, null, null)

        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertEquals(BigDecimal.valueOf(11), result.currentPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(20), result.currentPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(9), result.currentPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(20), result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(9), result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.TEN, result.currentPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.ZERO, result.currentPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.paymentReturn.debit)
        assertEquals(BigDecimal.ONE, result.currentPosition.paymentReturn.credit)
        assertEquals(BigDecimal.valueOf(9), result.currentPosition.paymentReturn.netPosition)
        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)
    }

    @Test
    fun `should get Self Funding Settlement Details DTO for 2 cycles if funding participant or intraday position gross not null `() {
        val participant = MockParticipants().getParticipant(false)
        val cycle = MockCycles().cycles
        val positions = MockPositions().positionDetails
        val fundingParticipant = MockParticipants().fundingParticipant
        val intraDayPositionGross = MockPositions().getIntraDaysFor(listOf("NDEASESSXXX"))

        val result = selfFundingSettlementDetailsMapper.presentFullParticipantSettlementDetails(cycle, positions, participant, fundingParticipant, intraDayPositionGross[0])

        assertEquals("DABASESX", result.settlementBank.id)
        assertEquals("Danske Bank AG", result.settlementBank.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.settlementBank.status)

        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertEquals(BigDecimal.valueOf(11), result.currentPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(20), result.currentPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(9), result.currentPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(20), result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(9), result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.TEN, result.currentPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.ZERO, result.currentPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.paymentReturn.debit)
        assertEquals(BigDecimal.ONE, result.currentPosition.paymentReturn.credit)
        assertEquals(BigDecimal.valueOf(9), result.currentPosition.paymentReturn.netPosition)
        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)

        assertEquals(BigDecimal.TEN, result.intraDayPositionGross.debitCap)
        assertEquals(BigDecimal.ONE, result.intraDayPositionGross.debitPosition)
    }

    @Test
    fun `should get Self Funding Settlement Details DTO for 1 cycle`() {
        val cycles = listOf(
                Cycle.builder()
                        .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                        .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                        .id("01")
                        .status(CycleStatus.COMPLETED)
                        .build())
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(true)

        val result = selfFundingSettlementDetailsMapper.presentOneCycleParticipantSettlementDetails(cycles, positionDetails, participant, null, null)
        assertNull(result.currentCycle)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertNull(result.currentPositionTotals)

        assertEquals(BigDecimal.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(20), result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(9), result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)

        assertEquals("HANDSESS", result.participant.bic)
        assertEquals("HANDSESS", result.participant.id)
        assertEquals("Svenska Handelsbanken", result.participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.participant.status)
        assertNotNull(result.participant.suspendedTime)
    }

    @Test
    fun `should get Self Funding Settlement Details DTO for 1 cycle if funding participant or intraday position gross not null`() {
        val cycles = listOf(
                Cycle.builder()
                        .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                        .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                        .id("01")
                        .status(CycleStatus.COMPLETED)
                        .build())
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(true)
        val fundingParticipant = MockParticipants().fundingParticipant
        val intraDayPositionGross = MockPositions().getIntraDaysFor(listOf("NDEASESSXXX"))

        val result = selfFundingSettlementDetailsMapper.presentOneCycleParticipantSettlementDetails(cycles, positionDetails, participant, fundingParticipant, intraDayPositionGross[0])

        assertEquals("DABASESX", result.settlementBank.id)
        assertEquals("Danske Bank AG", result.settlementBank.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.settlementBank.status)

        assertNull(result.currentCycle)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertNull(result.currentPositionTotals)

        assertEquals(BigDecimal.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.valueOf(20), result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.valueOf(9), result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)

        assertEquals("HANDSESS", result.participant.bic)
        assertEquals("HANDSESS", result.participant.id)
        assertEquals("Svenska Handelsbanken", result.participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.participant.status)
        assertNotNull(result.participant.suspendedTime)

        assertEquals(BigDecimal.TEN, result.intraDayPositionGross.debitCap)
        assertEquals(BigDecimal.ONE, result.intraDayPositionGross.debitPosition)
    }
}