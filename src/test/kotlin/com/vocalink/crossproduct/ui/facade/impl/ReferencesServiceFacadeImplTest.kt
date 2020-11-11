package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.reference.MessageDirectionReference
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.repository.FileRepository
import com.vocalink.crossproduct.repository.ParticipantRepository
import com.vocalink.crossproduct.repository.ReferencesRepository
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
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class ReferencesServiceFacadeImplTest {

    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val referencesRepository = mock(ReferencesRepository::class.java)!!
    private val fileRepository = mock(FileRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    @InjectMocks
    private lateinit var referenceServiceFacadeImpl: ReferencesServiceFacadeImpl

    @Test
    fun `should get participants name and bic`() {
        `when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        `when`(uiPresenter.presentParticipantReferences(any()))
                .thenReturn(any())

        referenceServiceFacadeImpl.getParticipantReferences(TestConstants.CONTEXT, ClientType.UI)

        verify(participantRepository, atLeastOnce()).findAll(TestConstants.CONTEXT)
        verify(presenterFactory, atLeastOnce()).getPresenter(any())
        verify(uiPresenter, atLeastOnce()).presentParticipantReferences(any())
    }

    @Test
    fun `should get alerts reference`() {
        val messageRefs = listOf(MessageDirectionReference.builder().build())
        val messageRefsDto = listOf(MessageDirectionReferenceDto.builder().build())

        `when`(referencesRepository
                .findMessageDirectionReferences(TestConstants.CONTEXT))
                .thenReturn(messageRefs)

        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentMessageDirectionReferences(any()))
                .thenReturn(messageRefsDto)

        val result = referenceServiceFacadeImpl.getMessageDirectionReferences(TestConstants.CONTEXT, ClientType.UI)

        verify(referencesRepository).findMessageDirectionReferences(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentMessageDirectionReferences(any())

        assertNotNull(result)
    }
}