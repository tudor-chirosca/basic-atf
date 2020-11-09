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
import org.mockito.Mockito
import kotlin.test.assertNotNull
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ReferencesServiceFacadeImplTest {

    private val participantRepository = Mockito.mock(ParticipantRepository::class.java)!!
    private val referencesRepository = Mockito.mock(ReferencesRepository::class.java)!!
    private val fileRepository = Mockito.mock(FileRepository::class.java)!!
    private val presenterFactory = Mockito.mock(PresenterFactory::class.java)!!
    private val uiPresenter = Mockito.mock(UIPresenter::class.java)!!

    @InjectMocks
    private lateinit var referenceServiceFacadeImpl: ReferencesServiceFacadeImpl


    @Test
    fun `should get participants name and bic`() {
        Mockito.`when`(participantRepository.findAll(TestConstants.CONTEXT))
                .thenReturn(MockParticipants().participants)
        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
        Mockito.`when`(uiPresenter.presentParticipantReferences(any()))
                .thenReturn(any())

        referenceServiceFacadeImpl.getParticipantReferences(TestConstants.CONTEXT, ClientType.UI)

        Mockito.verify(participantRepository, Mockito.atLeastOnce()).findAll(TestConstants.CONTEXT)
        Mockito.verify(presenterFactory, Mockito.atLeastOnce()).getPresenter(any())
        Mockito.verify(uiPresenter, Mockito.atLeastOnce()).presentParticipantReferences(any())
    }

    @Test
    fun `should get alerts reference`() {
        val messageRefs = listOf(MessageDirectionReference.builder().build())
        val messageRefsDto = listOf(MessageDirectionReferenceDto.builder().build())

        Mockito.`when`(referencesRepository
                .findMessageDirectionReferences(TestConstants.CONTEXT))
                .thenReturn(messageRefs)

        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        Mockito.`when`(uiPresenter.presentMessageDirectionReferences(any()))
                .thenReturn(messageRefsDto)

        val result = referenceServiceFacadeImpl.getMessageDirectionReferences(TestConstants.CONTEXT, ClientType.UI)

        Mockito.verify(referencesRepository).findMessageDirectionReferences(any())
        Mockito.verify(presenterFactory).getPresenter(any())
        Mockito.verify(uiPresenter).presentMessageDirectionReferences(any())

        assertNotNull(result)
    }
}