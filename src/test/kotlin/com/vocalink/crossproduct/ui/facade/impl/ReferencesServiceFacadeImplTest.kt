package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.repository.ParticipantRepository
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ReferencesServiceFacadeImplTest {

    @Mock
    private lateinit var participantRepository: ParticipantRepository

    @InjectMocks
    private lateinit var referenceServiceFacadeImpl: ReferencesServiceFacadeImpl

    @Mock
    private lateinit var presenterFactory: PresenterFactory

    @Mock
    private lateinit var uiPresenter: UIPresenter

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
}