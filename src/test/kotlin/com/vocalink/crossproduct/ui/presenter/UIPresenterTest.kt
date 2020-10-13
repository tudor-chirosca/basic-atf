package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.domain.Cycle
import com.vocalink.crossproduct.domain.CycleStatus
import com.vocalink.crossproduct.domain.ParticipantStatus
import com.vocalink.crossproduct.mocks.MockCycles
import com.vocalink.crossproduct.mocks.MockDashboardModels
import com.vocalink.crossproduct.mocks.MockIOData
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.mocks.MockPositions
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
class UIPresenterTest {

    private val selfFundingDetailsMapper = Mockito.mock(SelfFundingSettlementDetailsMapper::class.java)!!

    private val testingModule = UIPresenter(
            selfFundingDetailsMapper
    )

    @Test
    fun `should get Settlement Dashboard DTO`() {
        val cycles = MockCycles().cyclesWithPositions
        val participants = MockParticipants().participants

        val result = testingModule.presentSettlement(cycles, participants)

        assertEquals(3, result.positions.size)
        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)

        assertEquals("HANDSESS", result.positions[0].participant.id)
        assertEquals("HANDSESS", result.positions[0].participant.bic)
        assertEquals("Svenska Handelsbanken", result.positions[0].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.positions[0].participant.status)
        assertNotNull(result.positions[0].participant.suspendedTime)

        assertEquals(BigInteger.TEN, result.positions[0].currentPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[0].currentPosition.debit)
        assertEquals(BigInteger.ZERO, result.positions[0].currentPosition.netPosition)
        assertEquals(BigInteger.TEN, result.positions[0].previousPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[0].previousPosition.debit)
        assertEquals(BigInteger.ZERO, result.positions[0].previousPosition.netPosition)

        assertEquals("NDEASESSXXX", result.positions[1].participant.id)
        assertEquals("NDEASESSXXX", result.positions[1].participant.bic)
        assertEquals("Nordea", result.positions[1].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.positions[1].participant.status)
        assertNull(result.positions[1].participant.suspendedTime)

        assertEquals(BigInteger.ONE, result.positions[1].currentPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[1].currentPosition.debit)
        assertEquals(BigInteger.valueOf(9), result.positions[1].currentPosition.netPosition)
        assertEquals(BigInteger.ONE, result.positions[1].previousPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[1].previousPosition.debit)
        assertEquals(BigInteger.valueOf(9), result.positions[1].previousPosition.netPosition)
    }

    @Test
    fun `should get Self Funding Settlement Details DTO for 2 cycles`() {
        val cycles = MockCycles().cycles
        val positionDetails = MockPositions().positionDetails
        val participant = MockParticipants().getParticipant(true)

        Mockito.`when`(selfFundingDetailsMapper
                .presentFullParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(MockDashboardModels().getSelfFundingDetailsDto())

        val result = testingModule.presentParticipantSettlementDetails(cycles,
                positionDetails, participant, null, null)
        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertEquals(BigInteger.ZERO, result.currentPositionTotals.totalCredit)
        assertEquals(BigInteger.TEN, result.currentPositionTotals.totalDebit)
        assertEquals(BigInteger.TEN, result.currentPositionTotals.totalNetPosition)
        assertEquals(BigInteger.TEN, result.previousPositionTotals.totalCredit)
        assertEquals(BigInteger.TEN, result.previousPositionTotals.totalDebit)
        assertEquals(BigInteger.ZERO, result.previousPositionTotals.totalNetPosition)
        assertEquals(BigInteger.TEN, result.currentPosition.customerCreditTransfer.debit)
        assertEquals(BigInteger.ONE, result.currentPosition.customerCreditTransfer.credit)
        assertEquals(BigInteger.valueOf(9), result.currentPosition.customerCreditTransfer.netPosition)
        assertEquals(BigInteger.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigInteger.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigInteger.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigInteger.TEN, result.currentPosition.paymentReturn.debit)
        assertEquals(BigInteger.TEN, result.currentPosition.paymentReturn.credit)
        assertEquals(BigInteger.ZERO, result.currentPosition.paymentReturn.netPosition)
        assertEquals(BigInteger.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigInteger.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigInteger.ZERO, result.previousPosition.paymentReturn.netPosition)
        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)

        verify(selfFundingDetailsMapper).presentFullParticipantSettlementDetails(any(), any(), any(), any(), any())
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

        Mockito.`when`(selfFundingDetailsMapper
                .presentOneCycleParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(MockDashboardModels().getSelfFundingDetailsDtoForOneCycle())

        val result = testingModule.presentParticipantSettlementDetails(cycles, positionDetails, participant,
                null, null)
        assertNull(result.currentCycle)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertNull(result.currentPositionTotals)

        assertEquals(BigInteger.TEN, result.previousPositionTotals.totalCredit)
        assertEquals(BigInteger.TEN, result.previousPositionTotals.totalDebit)
        assertEquals(BigInteger.ZERO, result.previousPositionTotals.totalNetPosition)
        assertEquals(BigInteger.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigInteger.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigInteger.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigInteger.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigInteger.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigInteger.ZERO, result.previousPosition.paymentReturn.netPosition)

        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)

        verify(selfFundingDetailsMapper).presentOneCycleParticipantSettlementDetails(any(), any(), any(), any(), any())
    }

    @Test
    fun `should get participant IO data DTO`() {
        val date = LocalDate.now()
        val participants = MockParticipants().participants
        val ioData = MockIOData().getParticipantsIOData()

        val result = testingModule.presentInputOutput(participants, ioData, date)

        assertEquals("0.67", result.batchesRejected)
        assertEquals("0.67", result.filesRejected)
        assertEquals("0.67", result.transactionsRejected)

        assertEquals(3, result.rows.size)

        assertEquals("ESSESESS", result.rows[0].participant.id)
        assertEquals("ESSESESS", result.rows[0].participant.bic)
        assertEquals("SEB Bank", result.rows[0].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.rows[0].participant.status)
        assertNull(result.rows[0].participant.suspendedTime)

        assertEquals(1, result.rows[0].batches.submitted)
        assertEquals(1.00, result.rows[0].batches.rejected)
        assertEquals(1, result.rows[0].files.submitted)
        assertEquals(1.00, result.rows[0].files.rejected)
        assertEquals(1, result.rows[0].transactions.submitted)
        assertEquals(1.00, result.rows[0].transactions.rejected)

        assertEquals("HANDSESS", result.rows[1].participant.id)
        assertEquals("HANDSESS", result.rows[1].participant.bic)
        assertEquals("Svenska Handelsbanken", result.rows[1].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.rows[1].participant.status)
        assertNotNull(result.rows[1].participant.suspendedTime)

        assertEquals(10, result.rows[1].batches.submitted)
        assertEquals(1.00, result.rows[1].batches.rejected)
        assertEquals(10, result.rows[1].files.submitted)
        assertEquals(1.00, result.rows[1].files.rejected)
        assertEquals(10, result.rows[1].transactions.submitted)
        assertEquals(1.00, result.rows[1].transactions.rejected)

        assertEquals("NDEASESSXXX", result.rows[2].participant.id)
        assertEquals("NDEASESSXXX", result.rows[2].participant.bic)
        assertEquals("Nordea", result.rows[2].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.rows[2].participant.status)
        assertNull(result.rows[2].participant.suspendedTime)

        assertEquals(0, result.rows[2].batches.submitted)
        assertEquals(0.00, result.rows[2].batches.rejected)
        assertEquals(0, result.rows[2].files.submitted)
        assertEquals(0.00, result.rows[2].files.rejected)
        assertEquals(0, result.rows[2].transactions.submitted)
        assertEquals(0.00, result.rows[2].transactions.rejected)
    }

    @Test
    fun `should get UI ClientType`() {
        val result = testingModule.clientType
        assertEquals(ClientType.UI, result)
    }
}