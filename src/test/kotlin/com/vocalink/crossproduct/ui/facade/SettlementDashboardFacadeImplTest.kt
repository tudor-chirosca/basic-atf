package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.position.IntraDayPositionGross
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.domain.position.PositionRepository
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.infrastructure.exception.NonConsistentDataException
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto
import com.vocalink.crossproduct.ui.presenter.ClientType.UI
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.ZonedDateTime

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
        `when`(presenterFactory.getPresenter(UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should get settlement dto by participant id`() {
        val participantId = "NDEASESSXXX"
        val fundedParticipantId = MockParticipants().getParticipant(false).id
        val mockModel = SettlementDashboardDto.builder().build()
        val intraDay = IntraDayPositionGross(
                null, null,null,null,
                null
        )
        val cycles = listOf(
                Cycle.builder().build(),
                Cycle.builder().build()
        )
        `when`(cycleRepository.findAll())
                .thenReturn(cycles)
        `when`(participantRepository
                .findById(participantId))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(Page(2, MockParticipants().participants))
        `when`(intraDayPositionGrossRepository
                .findById(fundedParticipantId))
                .thenReturn(listOf(intraDay))
        `when`(uiPresenter.presentFundingParticipantSettlement(any(), any(), any(), any()))
                .thenReturn(mockModel)

        settlementServiceFacadeImpl.getParticipantSettlement(CONTEXT, UI, participantId)

        verify(intraDayPositionGrossRepository, atLeastOnce()).findById(any())
        verify(uiPresenter, atLeastOnce()).presentFundingParticipantSettlement(any(), any(), any(), any())

    }

    @Test
    fun `should throw error on cycles less than 2`() {
        `when`(participantRepository.findAll())
                .thenReturn(Page(2, MockParticipants().participants))
        `when`(cycleRepository.findAll())
                .thenReturn(emptyList())
        assertThrows(NonConsistentDataException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlement(CONTEXT, UI, null)
        }
    }

    @Test
    fun `should throw EntityNotFoundException on cycles less than 2 for participant settlement details`() {
        val participantId = "HANDSESS"
        `when`(participantRepository.findById(any()))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(emptyList())
        `when`(cycleRepository.findLatest(2))
                .thenReturn(emptyList())
        `when`(participantRepository.findAll())
                .thenReturn(Page(2, MockParticipants().participants))
        `when`(cycleRepository.findAll())
                .thenReturn(emptyList())
        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participantId)
        }
    }

    @Test
    fun `should throw EntityNotFoundException if no IntraDayPositionGross found`() {
        val participant = Participant.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .fundingBic("NDEASESSXXX")
                .name("Svenska Handelsbanken")
                .suspendedTime(ZonedDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .participantType(ParticipantType.FUNDED)
                .build()
        val positionsDetails = ParticipantPosition(
                null, null,null,null,null,
                null,null,null,null
        )
        val cycle = Cycle(
                null, null, null, null,null,
                null, null
        )
        `when`(participantRepository.findById(any()))
                .thenReturn(participant)
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(listOf(positionsDetails, positionsDetails))
        `when`(cycleRepository.findLatest(2))
                .thenReturn(listOf(cycle, cycle))
        `when`(intraDayPositionGrossRepository.findById(any()))
                .thenReturn(emptyList())

        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participant.id)
        }
    }

    @Test
    fun `should enter in presentFundedParticipantSettlementDetails if FUNDED participant`() {
        val participant = Participant.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .fundingBic("NDEASESSXXX")
                .name("Svenska Handelsbanken")
                .suspendedTime(ZonedDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .participantType(ParticipantType.FUNDED)
                .build()
        val positionsDetails = ParticipantPosition(
                null, null,null,null,null,
                null,null,null,null
        )
        val cycle = Cycle(
                null, null, null, null,null,
                null, null
        )
        val intraDay = IntraDayPositionGross(
                null, "HANDSESS",null,null,
                null
        )
        `when`(participantRepository.findById(any()))
                .thenReturn(participant)
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(listOf(positionsDetails, positionsDetails))
        `when`(cycleRepository.findLatest(2))
                .thenReturn(listOf(cycle, cycle))
        `when`(intraDayPositionGrossRepository.findById(any()))
                .thenReturn(listOf(intraDay))

        settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participant.id)

        verify(uiPresenter, atLeastOnce()).presentFundedParticipantSettlementDetails(
                any(), any(), any(), any(), any())

    }

    @Test
    fun `should enter in presentParticipantSettlementDetails if NOT FUNDED participant`() {
        val participant = Participant.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .fundingBic("NDEASESSXXX")
                .name("Svenska Handelsbanken")
                .suspendedTime(ZonedDateTime.now().plusDays(15))
                .status(ParticipantStatus.SUSPENDED)
                .participantType(ParticipantType.DIRECT)
                .build()
        val positionsDetails = ParticipantPosition(
                null, null,null,null,null,
                null,null,null,null
        )
        val cycle = Cycle(
                null, null, null, null,null,
                null, null
        )
        `when`(participantRepository.findById(any()))
                .thenReturn(participant)
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(listOf(positionsDetails, positionsDetails))
        `when`(cycleRepository.findLatest(2))
                .thenReturn(listOf(cycle, cycle))

        settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participant.id)

        verify(uiPresenter, atLeastOnce()).presentParticipantSettlementDetails(
                any(), any(), any())
    }

    @Test
    fun `should throw NonConsistentDataException when positions and cycle size differs`() {
        val participantId = "HANDSESS"
        val cycles = listOf(
                Cycle.builder().build(),
                Cycle.builder().build()
        )
        `when`(participantRepository.findById(any()))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(emptyList())
        `when`(cycleRepository.findLatest(2))
                .thenReturn(cycles)
        assertThrows(NonConsistentDataException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participantId)
        }
    }

    @Test
    fun `should throw error on settlements details if cycles are empty`() {
        val participantId = "HANDSESS"
        val positionsDetails = ParticipantPosition(
                null, null,null,null,null,
                null,null,null,null
        )
        `when`(participantRepository
                .findById(participantId))
                .thenReturn(MockParticipants().getParticipant(false))
        `when`(positionRepository
                .findByParticipantId(participantId))
                .thenReturn(listOf(positionsDetails))
        `when`(cycleRepository.findByIds(emptyList()))
                .thenReturn(emptyList())
        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participantId)
        }
    }

    @Test
    fun `should throw error if no funding Participant with given id`() {
        val participantId = "HANDSESS"
        val positionsDetails = ParticipantPosition(
               null, null,null,null,null,
                null,null,null,null
        )
        val mockModel = ParticipantDashboardSettlementDetailsDto(
                null, null,null,null,null,
                null,null,null,null
        )
        val cycles = listOf(
                Cycle.builder().build(),
                Cycle.builder().build()
        )
        `when`(participantRepository.findById(any()))
                .thenReturn(MockParticipants().getParticipant(true))
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(listOf(positionsDetails))
        `when`(cycleRepository.findByIds(any()))
                .thenReturn(cycles)
        `when`(uiPresenter.presentParticipantSettlementDetails(any(), any(), any()))
                .thenReturn(mockModel)

        assertThrows(EntityNotFoundException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participantId)
        }
    }

    @Test
    fun `should throw error if no Intra-Day Participant Position gross for participant id`() {
        val participantId = "ESSESESS"
        val positionsDetails = ParticipantPosition(
                null, null,null,null,null,
                null,null,null,null
        )
        val mockModel = ParticipantDashboardSettlementDetailsDto(
                null, null,null,null,null,
                null,null,null,null
        )
        val cycles = listOf(
                Cycle.builder().build(),
                Cycle.builder().build()
        )
        `when`(participantRepository.findById(any()))
                .thenReturn(selfFundingParticipant)
        `when`(positionRepository.findByParticipantId(any()))
                .thenReturn(listOf(positionsDetails))
        `when`(cycleRepository.findLatest(2))
                .thenReturn(cycles)
        `when`(uiPresenter.presentParticipantSettlementDetails(any(), any(), any()))
                .thenReturn(mockModel)

        assertThrows(NonConsistentDataException::class.java) {
            settlementServiceFacadeImpl.getParticipantSettlementDetails(CONTEXT, UI, participantId)
        }
    }

    @Test
    fun `should not return FUNDED and SCHEME OPERATOR participants`() {
        val typeDirect = ParticipantType.DIRECT
        val typeScheme = ParticipantType.SCHEME_OPERATOR
        val typeFunded = ParticipantType.FUNDED

        val participants = listOf(
            Participant.builder()
                .bic("NDEASESSXXY")
                .name("Svenska Handelsbanken")
                .participantType(typeDirect)
                .build(),
            Participant.builder()
                .bic("NDEASESSXXX")
                .name("Nordea")
                .participantType(typeScheme)
                .build(),
            Participant.builder()
                .bic("HANDSESS")
                .name("Svenska Handelsbanken")
                .participantType(typeFunded)
                .build()
        )
        val cycles = listOf(
                Cycle.builder().build(),
                Cycle.builder().build()
        )
        `when`(cycleRepository.findAll())
            .thenReturn(cycles)
        `when`(participantRepository.findAll())
            .thenReturn(Page(3, participants))
        `when`(presenterFactory.getPresenter(UI))
            .thenReturn(UIPresenter(null))

        val result = settlementServiceFacadeImpl.getParticipantSettlement(CONTEXT, UI, null)

        assertThat(result.positions.size).isEqualTo(1)
        assertThat(result.positions[0].participant.participantType).isEqualTo(typeDirect.toString())
    }
}
