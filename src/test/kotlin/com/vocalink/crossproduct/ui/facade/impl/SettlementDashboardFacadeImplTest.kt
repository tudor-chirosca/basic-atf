package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.cycle.CycleRepository
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository
import com.vocalink.crossproduct.domain.position.PositionDetails
import com.vocalink.crossproduct.domain.position.PositionRepository
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.stream.Collectors
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

open class SettlementDashboardFacadeImplTest {

    private val participantRepository = mock(ParticipantRepository::class.java)
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val positionRepository = mock(PositionRepository::class.java)!!
    private val cycleRepository = mock(CycleRepository::class.java)!!
    private val intraDayPositionGrossRepository = mock(IntraDayPositionGrossRepository::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val settlementServiceFacadeImpl = SettlementDashboardFacadeImpl(
            repositoryFactory, presenterFactory)

    private val selfFundingParticipant = Participant.builder()
            .id("ESSESESS")
            .bic("ESSESESS")
            .fundingBic("ESSESESS")
            .name("SEB Bank")
            .suspendedTime(null)
            .status(ParticipantStatus.ACTIVE)
            .build()

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(repositoryFactory.getCycleRepository(anyString()))
                .thenReturn(cycleRepository)
        `when`(repositoryFactory.getPositionRepository(anyString()))
                .thenReturn(positionRepository)
        `when`(repositoryFactory.getIntradayPositionGrossRepository(anyString()))
                .thenReturn(intraDayPositionGrossRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should get settlement dto for all participants`() {
        val mockModel = MockDashboardModels().getAllParticipantsSettlementDashboardDto()
        `when`(participantRepository.findAll())
                .thenReturn(MockParticipants().participants)
        `when`(cycleRepository.findAll())
                .thenReturn(MockCycles().cycles)
        `when`(uiPresenter.presentAllParticipantsSettlement(any(), any()))
                .thenReturn(mockModel)
        val result = settlementServiceFacadeImpl.getSettlement(TestConstants.CONTEXT, ClientType.UI)
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
        assertEquals(BigDecimal.ONE, result.positions[0].currentPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[0].currentPosition.debit)
        assertEquals(BigDecimal.valueOf(9), result.positions[0].currentPosition.netPosition)
        assertEquals(BigDecimal.TEN, result.positions[0].previousPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[0].previousPosition.debit)
        assertEquals(BigDecimal.ZERO, result.positions[0].previousPosition.netPosition)

        assertEquals("HANDSESS", result.positions[1].participant.id)
        assertEquals("HANDSESS", result.positions[1].participant.bic)
        assertEquals("Svenska Handelsbanken", result.positions[1].participant.name)
        assertEquals(ParticipantStatus.SUSPENDED, result.positions[1].participant.status)
        assertNotNull(result.positions[1].participant.suspendedTime)
        assertEquals(BigDecimal.TEN, result.positions[1].currentPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[1].currentPosition.debit)
        assertEquals(BigDecimal.ZERO, result.positions[1].currentPosition.netPosition)
        assertEquals(BigDecimal.ONE, result.positions[1].previousPosition.credit)
        assertEquals(BigDecimal.TEN, result.positions[1].previousPosition.debit)
        assertEquals(BigDecimal.valueOf(9), result.positions[1].previousPosition.netPosition)
    }

    @Test
    fun `should get settlement dto by participant id`() {
        val participantId = "NDEASESSXXX"
        val fundedParticipantId = MockParticipants().getParticipant(false).id
        val intraDayPositionsGross = MockPositions().getIntraDaysFor(listOf(fundedParticipantId))
        val mockModel = MockDashboardModels().getFundingParticipantsSettlementDashboardDto()

        `when`(cycleRepository.findAll())
                .thenReturn(MockCycles().cycles)
        `when`(participantRepository
                .findById(participantId))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(MockParticipants().participants)
        `when`(intraDayPositionGrossRepository
                .findById(fundedParticipantId))
                .thenReturn(intraDayPositionsGross)
        `when`(uiPresenter.presentFundingParticipantSettlement(any(), any(), any(), any()))
                .thenReturn(mockModel)

        val result = settlementServiceFacadeImpl.getParticipantSettlement(TestConstants.CONTEXT, ClientType.UI, participantId)

        verify(intraDayPositionGrossRepository, atLeastOnce()).findById(any())
        verify(uiPresenter, atLeastOnce()).presentFundingParticipantSettlement(any(), any(), any(), any())

        assertNotNull(result.fundingParticipant)
        assertNotNull(result.previousPositionTotals)
        assertNotNull(result.currentPositionTotals)
        assertNotNull(result.intraDayPositionTotals)

        assertEquals(participantId, result.fundingParticipant.id)
    }

    @Test
    fun `should get self funding settlement details`() {
        val mockModel = MockDashboardModels().getSelfFundingDetailsDto()
        val participantId = "HANDSESS"
        val positionsDetails = MockPositions().positionDetails
        val activeCycleIds = positionsDetails.stream().map { obj: PositionDetails -> obj.sessionCode }
                .collect(Collectors.toList())
        `when`(participantRepository.findById(participantId))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(positionRepository.findByParticipantId(participantId))
                .thenReturn(positionsDetails)
        `when`(cycleRepository.findByIds(activeCycleIds))
                .thenReturn(MockCycles().cycles)
        `when`(uiPresenter.presentParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(mockModel)
        val result = settlementServiceFacadeImpl.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        assertEquals("02", result.currentCycle.id)
        assertEquals(CycleStatus.OPEN, result.currentCycle.status)
        assertEquals("01", result.previousCycle.id)
        assertEquals(CycleStatus.COMPLETED, result.previousCycle.status)
        assertEquals(BigDecimal.ZERO, result.currentPositionTotals.totalCredit)
        assertEquals(BigDecimal.TEN, result.currentPositionTotals.totalDebit)
        assertEquals(BigDecimal.TEN, result.currentPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.previousPositionTotals.totalCredit)
        assertEquals(BigDecimal.TEN, result.previousPositionTotals.totalDebit)
        assertEquals(BigDecimal.ZERO, result.previousPositionTotals.totalNetPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.currentPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.currentPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.customerCreditTransfer.debit)
        assertEquals(BigDecimal.ONE, result.previousPosition.customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(9), result.previousPosition.customerCreditTransfer.netPosition)
        assertEquals(BigDecimal.TEN, result.currentPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.currentPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.currentPosition.paymentReturn.netPosition)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.debit)
        assertEquals(BigDecimal.TEN, result.previousPosition.paymentReturn.credit)
        assertEquals(BigDecimal.ZERO, result.previousPosition.paymentReturn.netPosition)
        assertEquals("NDEASESSXXX", result.participant.bic)
        assertEquals("NDEASESSXXX", result.participant.id)
        assertEquals("Nordea", result.participant.name)
        assertEquals(ParticipantStatus.ACTIVE, result.participant.status)
        assertNull(result.participant.suspendedTime)
    }

    @Test
    fun `should throw error on cycles less than 2`() {
        `when`(participantRepository.findAll())
                .thenReturn(MockParticipants().participants)
        `when`(cycleRepository.findAll())
                .thenReturn(emptyList())
        assertThrows(NonConsistentDataException::class.java) {
            settlementServiceFacadeImpl.getSettlement(TestConstants.CONTEXT, ClientType.UI)
        }
    }

    @Test
    fun `should throw error on settlements details if cycles size and position details size are different`() {
        val participantId = "HANDSESS"
        `when`(participantRepository.findById(participantId))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(positionRepository.findByParticipantId(participantId))
                .thenReturn(emptyList())
        `when`(cycleRepository.findByIds(emptyList()))
                .thenReturn(MockCycles().cycles)
        assertThrows(NonConsistentDataException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }

    @Test
    fun `should throw error on settlements details if cycles are empty`() {
        val participantId = "HANDSESS"
        val positionsDetails = MockPositions().positionDetails
        `when`(participantRepository
                .findById(participantId))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(positionRepository
                .findByParticipantId(participantId))
                .thenReturn(positionsDetails)
        `when`(cycleRepository.findByIds(emptyList()))
                .thenReturn(emptyList())
        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }

    @Test
    fun `should throw error if no funding Participant with given id`() {
        val participantId = "HANDSESS"
        val positionsDetails = MockPositions().positionDetails
        val mockModel = MockDashboardModels().getSelfFundingDetailsDto()
        val activeCycleIds = positionsDetails.stream().map { obj: PositionDetails -> obj.sessionCode }
                .collect(Collectors.toList())

        `when`(participantRepository.findById(participantId))
                .thenReturn(MockParticipants().getParticipant(true))
        `when`(positionRepository.findByParticipantId(participantId))
                .thenReturn(positionsDetails)
        `when`(cycleRepository.findByIds(activeCycleIds))
                .thenReturn(MockCycles().cycles)
        `when`(uiPresenter.presentParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(mockModel)

        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }

    @Test
    fun `should throw error if no Intra-Day Participant Position gross for participant id`() {
        val participantId = "ESSESESS"
        val positionsDetails = MockPositions().positionDetails
        val mockModel = MockDashboardModels().getSelfFundingDetailsDto()
        val activeCycleIds = positionsDetails.stream().map { obj: PositionDetails -> obj.sessionCode }
                .collect(Collectors.toList())

        `when`(participantRepository.findById(participantId))
                .thenReturn(selfFundingParticipant)
        `when`(positionRepository.findByParticipantId(participantId))
                .thenReturn(positionsDetails)
        `when`(cycleRepository.findByIds(activeCycleIds))
                .thenReturn(MockCycles().cycles)
        `when`(uiPresenter.presentParticipantSettlementDetails(any(), any(), any(), any(), any()))
                .thenReturn(mockModel)

        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId)
        }
    }
}
