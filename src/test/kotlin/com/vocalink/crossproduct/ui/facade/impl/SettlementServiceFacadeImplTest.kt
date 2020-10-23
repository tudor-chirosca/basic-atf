package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.repository.CycleRepository
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.repository.ParticipantRepository
import com.vocalink.crossproduct.domain.position.PositionDetails
import com.vocalink.crossproduct.repository.IntraDayPositionGrossRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.repository.PositionDetailsRepository
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException
import com.vocalink.crossproduct.mocks.MockCycles
import com.vocalink.crossproduct.mocks.MockDashboardModels
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.mocks.MockPositions
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigInteger
import java.util.Optional
import java.util.stream.Collectors
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class)
open class SettlementServiceFacadeImplTest {

    private val participantRepository = Mockito.mock(ParticipantRepository::class.java)!!
    private val presenterFactory = Mockito.mock(PresenterFactory::class.java)!!
    private val positionDetailsRepository = Mockito.mock(PositionDetailsRepository::class.java)!!
    private val cycleRepository = Mockito.mock(CycleRepository::class.java)!!
    private val intraDayPositionGrossRepository = Mockito.mock(IntraDayPositionGrossRepository::class.java)!!

    private val uiPresenter = Mockito.mock(UIPresenter::class.java)!!

    private var testingModule = SettlementServiceFacadeImpl(
            participantRepository,
            cycleRepository,
            presenterFactory,
            positionDetailsRepository,
            intraDayPositionGrossRepository
    )

    @Test
    fun `should get settlement dto`() {
        val mockModel = MockDashboardModels().getSettlementDashboardDto()
        Mockito.`when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        Mockito.`when`(cycleRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockCycles().cycles)
        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        Mockito.`when`(uiPresenter.presentSettlement(any(), any(), any(), any()))
                .thenReturn(mockModel)
        val result = testingModule.getSettlement(TestConstants.CONTEXT, ClientType.UI, null)
        assertEquals(2, result.positions.size)
        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)

        assertEquals("NDEASESSXXX", result.positions[0].participant.id)
        assertEquals("NDEASESSXXX", result.positions[0].participant.bic)
        assertEquals("Nordea", result.positions[0].participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.positions[0].participant.status)
        assertNull(result.positions[0].participant.suspendedTime)
        assertEquals(BigInteger.ONE, result.positions[0].currentPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[0].currentPosition.debit)
        assertEquals(BigInteger.valueOf(9), result.positions[0].currentPosition.netPosition)
        assertEquals(BigInteger.TEN, result.positions[0].previousPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[0].previousPosition.debit)
        assertEquals(BigInteger.ZERO, result.positions[0].previousPosition.netPosition)

        assertEquals("HANDSESS", result.positions[1].participant.id)
        assertEquals("HANDSESS", result.positions[1].participant.bic)
        assertEquals("Svenska Handelsbanken", result.positions[1].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.positions[1].participant.status)
        assertNotNull(result.positions[1].participant.suspendedTime)
        assertEquals(BigInteger.TEN, result.positions[1].currentPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[1].currentPosition.debit)
        assertEquals(BigInteger.ZERO, result.positions[1].currentPosition.netPosition)
        assertEquals(BigInteger.ONE, result.positions[1].previousPosition.credit)
        assertEquals(BigInteger.TEN, result.positions[1].previousPosition.debit)
        assertEquals(BigInteger.valueOf(9), result.positions[1].previousPosition.netPosition)
    }

    @Test
    fun `should get self funding settlement details`() {
        val mockModel = MockDashboardModels().getSelfFundingDetailsDto()
        val participantId = "HANDSESS"
        val positionsDetails = MockPositions().positionDetails
        val activeCycleIds = positionsDetails.stream().map { obj: PositionDetails -> obj.sessionCode }
                .collect(Collectors.toList())
        Mockito.`when`(participantRepository.findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.of(MockParticipants().getParticipant(false)))
        Mockito.`when`(positionDetailsRepository.findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(positionsDetails)
        Mockito.`when`(cycleRepository.findByIds(TestConstants.CONTEXT, activeCycleIds))
                .thenReturn(MockCycles().cycles)
        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        Mockito.`when`(uiPresenter.presentParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(mockModel)
        val result = testingModule.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
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
    }

    @Test
    fun `should throw error on cycles less than 2`() {
        val participantId = "HANDSESS"
        Mockito.`when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        Mockito.`when`(cycleRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(emptyList())
        assertThrows(NonConsistentDataException::class.java) {
            testingModule.getSettlement(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }

    @Test
    fun `should throw error on settlement details if no participants for given id`() {
        val participantId = "fake_id"
        Mockito.`when`(participantRepository.findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.empty())
        assertThrows(EntityNotFoundException::class.java) {
            testingModule.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }

    @Test
    fun `should throw error on settlements details if cycles size and position details size are different`() {
        val participantId = "HANDSESS"
        Mockito.`when`(participantRepository.findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.of(MockParticipants().getParticipant(false)))
        Mockito.`when`(positionDetailsRepository.findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(emptyList())
        Mockito.`when`(cycleRepository.findByIds(TestConstants.CONTEXT, emptyList()))
                .thenReturn(MockCycles().cycles)
        assertThrows(NonConsistentDataException::class.java) {
            testingModule.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }

    @Test
    fun `should throw error on settlements details if cycles are empty`() {
        val participantId = "HANDSESS"
        val positionsDetails = MockPositions().positionDetails
        Mockito.`when`(participantRepository
                .findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(Optional.of(MockParticipants().getParticipant(false)))
        Mockito.`when`(positionDetailsRepository
                .findByParticipantId(TestConstants.CONTEXT, participantId))
                .thenReturn(positionsDetails)
        Mockito.`when`(cycleRepository.findByIds(TestConstants.CONTEXT, emptyList()))
                .thenReturn(emptyList())
        assertThrows(EntityNotFoundException::class.java) {
            testingModule.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }
}
