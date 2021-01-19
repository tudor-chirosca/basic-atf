package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleRepository
import com.vocalink.crossproduct.domain.files.FileReference
import com.vocalink.crossproduct.domain.files.FileRepository
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.domain.reference.ReferencesRepository
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants.PRODUCT
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.reference.FileStatusesTypeDto
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import java.time.LocalDate
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ReferencesServiceFacadeImplTest {

    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val referencesRepository = mock(ReferencesRepository::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)!!
    private val fileRepository = mock(FileRepository::class.java)!!
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
        `when`(repositoryFactory.getFileRepository(anyString()))
                .thenReturn(fileRepository)
        `when`(repositoryFactory.getCycleRepository(anyString()))
                .thenReturn(cycleRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should get participants name and bic`() {
        `when`(participantRepository.findAll())
                .thenReturn(MockParticipants().participants)
        `when`(uiPresenter.presentParticipantReferences(any()))
                .thenReturn(listOf(ParticipantReferenceDto(
                        "", "", ParticipantType.FUNDED, "")))

        referenceServiceFacadeImpl.getParticipantReferences(CONTEXT, ClientType.UI)

        verify(participantRepository, atLeastOnce()).findAll()
        verify(presenterFactory, atLeastOnce()).getPresenter(any())
        verify(uiPresenter, atLeastOnce()).presentParticipantReferences(any())
    }

    @Test
    fun `should get alerts reference`() {
        val messageRefs = listOf(MessageDirectionReference.builder().build())
        val messageRefsDto = listOf(MessageDirectionReferenceDto.builder().build())

        `when`(repositoryFactory.getReferencesRepository(PRODUCT))
                .thenReturn(referencesRepository)

        `when`(referencesRepository.findAll())
                .thenReturn(messageRefs)

        `when`(uiPresenter.presentMessageDirectionReferences(any()))
                .thenReturn(messageRefsDto)

        val result = referenceServiceFacadeImpl.getMessageDirectionReferences(PRODUCT, ClientType.UI)

        verify(referencesRepository).findAll()
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentMessageDirectionReferences(any())

        assertNotNull(result)
    }

    @Test
    fun `should get cycles by date`() {
        val date = LocalDate.of(2020, 11, 3)
        val cycles = listOf(Cycle.builder().build())
        val cyclesDto = listOf(CycleDto.builder().build())

        `when`(cycleRepository.findByDate(date)).thenReturn(cycles)

        `when`(uiPresenter.presentCycleDateReferences(any())).thenReturn(cyclesDto)

        val result = referenceServiceFacadeImpl.getCyclesByDate(CONTEXT, ClientType.UI, date)

        verify(cycleRepository).findByDate(date)
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentCycleDateReferences(cycles)

        assertNotNull(result)
    }

    @Test
    fun `should get file references`() {
        val fileRefs = listOf(FileReference.builder().build())
        val fileStatsDto = listOf(FileStatusesTypeDto.builder().build())

        `when`(fileRepository
                .findFileReferences())
                .thenReturn(fileRefs)

        `when`(uiPresenter.presentFileReferencesFor(any(), any()))
                .thenReturn(fileStatsDto)

        val result = referenceServiceFacadeImpl.getFileReferences(CONTEXT, ClientType.UI, "")

        verify(fileRepository).findFileReferences()
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentFileReferencesFor(any(), any())

        assertNotNull(result)
    }
}
