package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertNotNull

class ParticipantFacadeImplTest {

    private val participantRepository = mock(ParticipantRepository::class.java)
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val participantFacadeImpl = ParticipantFacadeImpl(
            repositoryFactory,
            presenterFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on funding participants`() {
        val page = Page<Participant>(1, listOf(Participant(null, null, null,
                null, null, null, ParticipantType.FUNDING, null,
                null, null, null, null, null)))
        val pageDto = PageDto<ManagedParticipantDto>(1, listOf(ManagedParticipantDto(null, null, null, null,
                null, null, null, null,
                null, null, null, 0)))
        val request = ManagedParticipantsSearchRequest()


        `when`(participantRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(listOf(Participant(null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null)))

        `when`(uiPresenter.presentManagedParticipants(any()))
                .thenReturn(pageDto)

        val result = participantFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(participantRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipants(any())
        verify(participantRepository).findByConnectingPartyAndType(any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should not invoke presenter and repository when participant type is funded`() {
        val page = Page<Participant>(1, listOf(Participant(null, null, null,
                null, null, null, ParticipantType.FUNDED, null,
                null, null, null, null, null)))
        val pageDto = PageDto<ManagedParticipantDto>(1, listOf(ManagedParticipantDto(null, null, null, null,
                null, null, null, null,
                null, null, null, 0)))
        val request = ManagedParticipantsSearchRequest()


        `when`(participantRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(listOf(Participant(null, null, null,
                        null, null, null, null, null,
                        null, null, null, null, null)))

        `when`(uiPresenter.presentManagedParticipants(any()))
                .thenReturn(pageDto)

        val result = participantFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(participantRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipants(any())

        assertNotNull(result)
    }
}