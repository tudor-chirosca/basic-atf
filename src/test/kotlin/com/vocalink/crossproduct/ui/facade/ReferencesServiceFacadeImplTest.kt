package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.cycle.CycleRepository
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.cycle.DayCycle
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.reference.EnquiryType
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ReasonCodeValidation
import com.vocalink.crossproduct.domain.reference.ReferencesRepository
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants.PRODUCT
import com.vocalink.crossproduct.ui.dto.reference.ReasonCodeReferenceDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ReferencesServiceFacadeImplTest {

    inline fun <reified T : Any> argumentCaptor() = ArgumentCaptor.forClass(T::class.java)

    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val referencesRepository = mock(ReferencesRepository::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val cycleRepository = mock(CycleRepository::class.java)

    private var referenceServiceFacadeImpl = ReferencesServiceFacadeImpl(
            repositoryFactory,
            presenterFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(repositoryFactory.getReferencesRepository(anyString()))
                .thenReturn(referencesRepository)
        `when`(repositoryFactory.getCycleRepository(anyString()))
                .thenReturn(cycleRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should get participants name and bic`() {
        val participants = listOf(
                Participant.builder()
                        .id("ESSESESS")
                        .bic("ESSESESS")
                        .fundingBic("NA")
                        .name("SEB Bank")
                        .suspendedTime(null)
                        .status(ParticipantStatus.ACTIVE)
                        .participantType(ParticipantType.FUNDING)
                        .schemeCode("P27-SEK")
                        .build(),
                Participant.builder()
                        .id("HANDSESS")
                        .bic("HANDSESS")
                        .fundingBic("NDEASESSXXX")
                        .name("Svenska Handelsbanken")
                        .suspendedTime(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(15))
                        .status(ParticipantStatus.SUSPENDED)
                        .participantType(ParticipantType.DIRECT)
                        .schemeCode("P27-SEK")
                        .build(),
                Participant.builder()
                        .id("NDEASESSXXX")
                        .bic("NDEASESSXXX")
                        .fundingBic("NA")
                        .name("Nordea")
                        .suspendedTime(null)
                        .status(ParticipantStatus.ACTIVE)
                        .participantType(ParticipantType.DIRECT)
                        .schemeCode("P27-SEK")
                        .build()
        )
        `when`(participantRepository.findAll())
                .thenReturn(Page(3, participants))

        referenceServiceFacadeImpl.getParticipantReferences(CONTEXT, ClientType.UI)

        verify(participantRepository, atLeastOnce()).findAll()
        verify(presenterFactory, atLeastOnce()).getPresenter(any())
    }

    @Test
    fun `should get alerts reference`() {
        val messageRefs = listOf(MessageDirectionReference.builder().build())

        `when`(repositoryFactory.getReferencesRepository(PRODUCT))
                .thenReturn(referencesRepository)

        `when`(referencesRepository.findMessageDirectionReferences())
                .thenReturn(messageRefs)

        val result = referenceServiceFacadeImpl.getMessageDirectionReferences(PRODUCT, ClientType.UI)

        verify(referencesRepository).findMessageDirectionReferences()
        verify(presenterFactory).getPresenter(any())

        assertNotNull(result)
    }

    @Test
    fun `should get cycles by date`() {
        val date = LocalDate.of(2020, 11, 3)
        val cycles = listOf(DayCycle(
                "cycleCode",
                "sessionCode",
                "sessionInstanceId",
                CycleStatus.OPEN,
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC"))
        ), DayCycle(
                "cycleCode",
                "sessionCode",
                "sessionInstanceId",
                CycleStatus.COMPLETED,
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC"))
        ))
        val captor = argumentCaptor<List<DayCycle>>()
        `when`(cycleRepository.findByDate(date))
                .thenReturn(cycles)
        `when`(uiPresenter.presentCycleDateReferences(captor.capture()))
                .thenReturn(emptyList())
        referenceServiceFacadeImpl.getDayCyclesByDate(CONTEXT, ClientType.UI, date, false)

        val result = captor.value
        verify(cycleRepository).findByDate(date)
        verify(presenterFactory).getPresenter(any())
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].status).isEqualTo(CycleStatus.OPEN)
        assertThat(result[1].status).isEqualTo(CycleStatus.COMPLETED)
    }

    @Test
    fun `should filter remove OPEN cycles when settled`() {
        val date = LocalDate.of(2020, 11, 3)
        val cycles = listOf(DayCycle(
                "cycleCode",
                "sessionCode",
                "sessionInstanceId",
                CycleStatus.OPEN,
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC"))
        ), DayCycle(
                "cycleCode",
                "sessionCode",
                "sessionInstanceId",
                CycleStatus.COMPLETED,
                ZonedDateTime.now(ZoneId.of("UTC")),
                ZonedDateTime.now(ZoneId.of("UTC"))
        ))
        `when`(cycleRepository.findByDate(date))
                .thenReturn(cycles)

        val captor = argumentCaptor<List<DayCycle>>()

        `when`(uiPresenter.presentCycleDateReferences(captor.capture()))
                .thenReturn(emptyList())

        referenceServiceFacadeImpl.getDayCyclesByDate(CONTEXT, ClientType.UI, date, true)

        val result = captor.value
        verify(cycleRepository).findByDate(date)
        verify(presenterFactory).getPresenter(any())
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].status).isEqualTo(CycleStatus.COMPLETED)
    }

    @Test
    fun `should get reason codes filtered by enquiry type`() {
        val reasonCodes = listOf(ReasonCodeValidation.ReasonCode(
                "F01",
                "description",
                true
        ))
        val validations = listOf(
                ReasonCodeValidation(EnquiryType.FILES, reasonCodes),
                ReasonCodeValidation(EnquiryType.BATCHES, reasonCodes),
                ReasonCodeValidation(EnquiryType.TRANSACTIONS, reasonCodes)
        )
        val statuses = listOf("ACK", "NAK")

        `when`(referencesRepository.findReasonCodes())
                .thenReturn(validations)

        `when`(referencesRepository.findStatuses("FILES"))
                .thenReturn(statuses)

        val captor = argumentCaptor<ReasonCodeValidation>()

        `when`(uiPresenter.presentReasonCodeReferences(captor.capture(), any()))
                .thenReturn(listOf(ReasonCodeReferenceDto.builder().build()))

        val result = referenceServiceFacadeImpl
                .getReasonCodeReferences(CONTEXT, ClientType.UI, "FILES")

        val filteredReasonCode = captor.value
        assertNotNull(filteredReasonCode)
        assertThat(filteredReasonCode.validationLevel).isEqualTo(EnquiryType.FILES)
        assertNotNull(result)
    }
}
