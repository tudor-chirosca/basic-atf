package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.repository.CycleRepository
import com.vocalink.crossproduct.repository.FileRepository
import com.vocalink.crossproduct.repository.ParticipantRepository
import com.vocalink.crossproduct.repository.ReferencesRepository
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.reference.MessageDirectionReferenceDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class ReferencesServiceFacadeImplTest {

    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val referencesRepository = mock(ReferencesRepository::class.java)!!
    private val fileRepository = mock(FileRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val cycleRepository = mock(CycleRepository::class.java)

    @InjectMocks
    private lateinit var referenceServiceFacadeImpl: ReferencesServiceFacadeImpl

    @Test
    fun `should get participants name and bic`() {
        `when`(participantRepository.findAll(CONTEXT))
                .thenReturn(MockParticipants().participants)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        `when`(uiPresenter.presentParticipantReferences(any()))
                .thenReturn(any())

        referenceServiceFacadeImpl.getParticipantReferences(CONTEXT, ClientType.UI)

        verify(participantRepository, atLeastOnce()).findAll(CONTEXT)
        verify(presenterFactory, atLeastOnce()).getPresenter(any())
        verify(uiPresenter, atLeastOnce()).presentParticipantReferences(any())
    }

    @Test
    fun `should get alerts reference`() {
        val messageRefs = listOf(MessageDirectionReference.builder().build())
        val messageRefsDto = listOf(MessageDirectionReferenceDto.builder().build())

        `when`(referencesRepository
                .findMessageDirectionReferences(CONTEXT))
                .thenReturn(messageRefs)

        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentMessageDirectionReferences(any()))
                .thenReturn(messageRefsDto)

        val result = referenceServiceFacadeImpl.getMessageDirectionReferences(CONTEXT, ClientType.UI)

        verify(referencesRepository).findMessageDirectionReferences(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentMessageDirectionReferences(any())

        assertNotNull(result)
    }

    @Test
    fun `should get cycles by date`() {
        val date = LocalDate.of(2020, 11, 3)
        val cycles = listOf(Cycle.builder().build())
        val cyclesDto = listOf(CycleDto.builder().build())

        `when`(cycleRepository.findCyclesByDate(CONTEXT, date)).thenReturn(cycles)

        `when`(presenterFactory.getPresenter(ClientType.UI)).thenReturn(uiPresenter)

        `when`(uiPresenter.presentCycleDateReferences(any())).thenReturn(cyclesDto)

        val result = referenceServiceFacadeImpl.getCyclesByDate(CONTEXT, ClientType.UI, date)

        verify(cycleRepository).findCyclesByDate(CONTEXT, date)
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentCycleDateReferences(cycles)

        assertNotNull(result)
    }
}