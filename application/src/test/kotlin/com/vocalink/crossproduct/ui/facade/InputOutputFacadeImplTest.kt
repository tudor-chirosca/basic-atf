package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.io.IOBatchesMessageTypes
import com.vocalink.crossproduct.domain.io.IODashboard
import com.vocalink.crossproduct.domain.io.IOData
import com.vocalink.crossproduct.domain.io.IODataDetails
import com.vocalink.crossproduct.domain.io.IODetails
import com.vocalink.crossproduct.domain.io.ParticipantIOData
import com.vocalink.crossproduct.domain.io.ParticipantIODataRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants.PRODUCT
import com.vocalink.crossproduct.ui.dto.IODashboardDto
import com.vocalink.crossproduct.ui.dto.io.IOBatchesMessageTypesDto
import com.vocalink.crossproduct.ui.dto.io.IODataDetailsDto
import com.vocalink.crossproduct.ui.dto.io.IODataDto
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto
import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.LocalDate

class InputOutputFacadeImplTest {

    private val participantIODataRepository = mock(ParticipantIODataRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private var inputOutputFacadeImpl = InputOutputFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(repositoryFactory.getParticipantsIODataRepository(anyString()))
                .thenReturn(participantIODataRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should get participant IO data`() {

        val participant = Participant.builder().id("id").name("Name").build()

        val participantIOData = ParticipantIOData.builder()
                .schemeParticipantIdentifier("ESSESESS")
                .batches(IOData.builder()
                        .submitted(1)
                        .rejected("1.00")
                        .build())
                .files(IOData.builder()
                        .submitted(1)
                        .rejected("1.00")
                        .build())
                .transactions(IOData.builder()
                        .submitted(1)
                        .rejected("1.00")
                        .build())
                .build()

        val participantIODataDto = ParticipantIODataDto.builder()
                .participant(ParticipantDto.builder()
                        .id("ESSESESS")
                        .bic("ESSESESS")
                        .name("SEB Bank")
                        .suspendedTime(null)
                        .status(ParticipantStatus.ACTIVE)
                        .participantType(ParticipantType.DIRECT)
                        .build())
                .batches(IODataDto.builder()
                        .submitted(1)
                        .rejected(1.00)
                        .build())
                .files(IODataDto.builder()
                        .submitted(1)
                        .rejected(1.00)
                        .build()).transactions(IODataDto.builder()
                        .submitted(1)
                        .rejected(1.00)
                        .build())
                .build()

        val ioDashboardDto = IODashboardDto.builder()
                .rows(listOf(participantIODataDto))
                .batchesRejected("2.00")
                .filesRejected("2.00")
                .transactionsRejected("2.00")
                .dateFrom(LocalDate.now())
                .build()

        val ioDashboard = IODashboard("2.00", "2.00",
                "2.00", listOf(participantIOData))

        `when`(participantRepository.findAll())
                .thenReturn(Page(1, listOf(participant)))
        `when`(participantIODataRepository.findAll())
                .thenReturn(ioDashboard)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        `when`(uiPresenter.presentInputOutput(any(), any(), any()))
                .thenReturn(ioDashboardDto)

        val result = inputOutputFacadeImpl.getInputOutputDashboard(PRODUCT, ClientType.UI, LocalDate.now())

        assertThat(result).isNotNull
    }

    @Test
    fun `should get participant IO details`() {

        val participant = Participant.builder().id("id").name("Name").build()

        val ioDetails = IODetails.builder()
                .batches(listOf(IOBatchesMessageTypes.builder()
                        .code("Pacs.008")
                        .name("Customer Credit Transfer")
                        .build()))
                .transactions(emptyList())
                .files(IODataDetails.builder().build())
                .build()

        val ioDetailsDto = IODetailsDto.builder()
                .participant(ParticipantDto.builder()
                        .build())
                .dateFrom(null)
                .batches(listOf(IOBatchesMessageTypesDto.builder()
                        .build()))
                .transactions(emptyList())
                .files(IODataDetailsDto.builder().build())
                .build()

        `when`(participantRepository.findById("id"))
                .thenReturn(participant)
        `when`(participantIODataRepository.findByParticipantId("id"))
                .thenReturn(ioDetails)
        `when`(uiPresenter.presentIoDetails(participant, ioDetails, LocalDate.now()))
                .thenReturn(ioDetailsDto)

        val result = inputOutputFacadeImpl.getInputOutputDetails(PRODUCT, ClientType.UI, "id", LocalDate.now())

        verify(participantRepository, atLeastOnce()).findById(any())
        verify(participantIODataRepository, atLeastOnce()).findByParticipantId(any())
        verify(uiPresenter, atLeastOnce()).presentIoDetails(any(), any(), any())

        assertThat(result).isNotNull
    }
}
