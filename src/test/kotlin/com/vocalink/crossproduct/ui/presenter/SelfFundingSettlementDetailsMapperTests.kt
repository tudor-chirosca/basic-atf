package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.domain.Cycle
import com.vocalink.crossproduct.domain.CycleStatus
import com.vocalink.crossproduct.domain.ParticipantStatus
import com.vocalink.crossproduct.mocks.MockCycles
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.mocks.MockPositions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
class SelfFundingSettlementDetailsMapperTests {

    private val testingModule = SelfFundingSettlementDetailsMapper()

    @Test
    fun `should get Self Funding Settlement Details DTO for 2 cycles`() {
        val cycles = MockCycles().cycles
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(false)

        val result = testingModule.presentFullSelfFundingSettlementDetails(cycles, positionDetails, participant)

        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertEquals(BigInteger.valueOf(11), result.currentPositionTotals.totalCredit)
        assertEquals(BigInteger.valueOf(20), result.currentPositionTotals.totalDebit)
        assertEquals(BigInteger.valueOf(9), result.currentPositionTotals.totalNetPosition)
        assertEquals(BigInteger.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigInteger.valueOf(20), result.previousPositionTotals.totalDebit)
        assertEquals(BigInteger.valueOf(9), result.previousPositionTotals.totalNetPosition)
        assertEquals(BigInteger.TEN, result.customerCreditTransfer.currentPosition.debit)
        assertEquals(BigInteger.TEN, result.customerCreditTransfer.currentPosition.credit)
        assertEquals(BigInteger.ZERO, result.customerCreditTransfer.currentPosition.netPosition)
        assertEquals(BigInteger.TEN, result.customerCreditTransfer.previousPosition.debit)
        assertEquals(BigInteger.ONE, result.customerCreditTransfer.previousPosition.credit)
        assertEquals(BigInteger.valueOf(9), result.customerCreditTransfer.previousPosition.netPosition)
        assertEquals(BigInteger.TEN, result.paymentReturn.previousPosition.debit)
        assertEquals(BigInteger.TEN, result.paymentReturn.previousPosition.credit)
        assertEquals(BigInteger.ZERO, result.paymentReturn.previousPosition.netPosition)
        assertEquals(BigInteger.TEN, result.paymentReturn.currentPosition.debit)
        assertEquals(BigInteger.ONE, result.paymentReturn.currentPosition.credit)
        assertEquals(BigInteger.valueOf(9), result.paymentReturn.currentPosition.netPosition)
        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)
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

        val result = testingModule.presentOneCycleSelfFundingSettlementDetails(cycles, positionDetails, participant)
        assertNull(result.currentCycle)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertNull(result.currentPositionTotals)

        assertEquals(BigInteger.valueOf(11), result.previousPositionTotals.totalCredit)
        assertEquals(BigInteger.valueOf(20), result.previousPositionTotals.totalDebit)
        assertEquals(BigInteger.valueOf(9), result.previousPositionTotals.totalNetPosition)
        assertEquals(BigInteger.TEN, result.customerCreditTransfer.previousPosition.debit)
        assertEquals(BigInteger.ONE, result.customerCreditTransfer.previousPosition.credit)
        assertEquals(BigInteger.valueOf(9), result.customerCreditTransfer.previousPosition.netPosition)
        assertEquals(BigInteger.TEN, result.paymentReturn.previousPosition.debit)
        assertEquals(BigInteger.TEN, result.paymentReturn.previousPosition.credit)
        assertEquals(BigInteger.ZERO, result.paymentReturn.previousPosition.netPosition)

        assertEquals("HANDSESS", result.participant.bic)
        assertEquals("HANDSESS", result.participant.id)
        assertEquals("Svenska Handelsbanken", result.participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.participant.status)
        assertNotNull(result.participant.suspendedTime)
    }
}