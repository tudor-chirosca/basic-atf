package com.vocalink.crossproduct.ui.facade


import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.broadcasts.Broadcast
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.ZonedDateTime

class BroadcastsFacadeImplTest {

    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val presenterFactory = mock(PresenterFactory::class.java)
    private val broadcastsRepository = mock(BroadcastsRepository::class.java)
    private val participantRepository = mock(ParticipantRepository::class.java)
    private val facadeImpl = BroadcastsFacadeImpl(repositoryFactory, presenterFactory)
    private val clock = FIXED_CLOCK

    companion object {
        var PRODUCT = "BPS"
        var CLIENT_TYPE = ClientType.UI
    }

    @Test
    fun `should return page with no results`() {

        val pageDto = Page<Broadcast>(0, listOf())

        `when`(repositoryFactory.getBroadcastsRepository(any())).thenReturn(broadcastsRepository)
        `when`(broadcastsRepository.findPaginated(any())).thenReturn(pageDto)

        `when`(repositoryFactory.getParticipantRepository(any())).thenReturn(participantRepository)
        `when`(participantRepository.findById(any())).thenReturn(null)

        `when`(presenterFactory.getPresenter(any())).thenReturn(UIPresenter(null, clock))

        val searchParameters = BroadcastsSearchParameters()

        val pageBroadcast = facadeImpl.getPaginated(PRODUCT, CLIENT_TYPE, searchParameters)

        assertThat(pageBroadcast.totalResults).isEqualTo(0)
        assertThat(pageBroadcast.items.size).isEqualTo(0)
    }

    @Test
    fun `broadcast should contain mapped participant reference dto`() {
        val participantId = "00000001"

        val broadcastDto = Broadcast(ZonedDateTime.parse("2021-01-22T00:00:00Z"), "id", "msg", listOf("00000001"))
        val pageDto = Page<Broadcast>(1, listOf(broadcastDto))

        `when`(repositoryFactory.getBroadcastsRepository(any())).thenReturn(broadcastsRepository)
        `when`(broadcastsRepository.findPaginated(any())).thenReturn(pageDto)

        val participant = Participant.builder().id(participantId).build()

        `when`(repositoryFactory.getParticipantRepository(any())).thenReturn(participantRepository)
        `when`(participantRepository.findById(participantId)).thenReturn(participant)

        `when`(presenterFactory.getPresenter(any())).thenReturn(UIPresenter(null, clock))

        val searchParameters = BroadcastsSearchParameters()

        val pageBroadcast = facadeImpl.getPaginated(PRODUCT, CLIENT_TYPE, searchParameters)

        assertThat(pageBroadcast).isNotNull
        assertThat(pageBroadcast.totalResults).isEqualTo(1)
        assertThat(pageBroadcast.items.size).isEqualTo(1)
        assertThat(pageBroadcast.items[0].recipients[0]).isExactlyInstanceOf(ParticipantReferenceDto::class.java)
    }

    @Test
    fun `should call participant repository`() {
        val participantId = "00000001"

        val broadcastDto = Broadcast(ZonedDateTime.parse("2021-01-22T00:00:00Z"), "id", "msg", listOf(participantId))
        val pageDto = Page<Broadcast>(1, listOf(broadcastDto))

        `when`(repositoryFactory.getBroadcastsRepository(any())).thenReturn(broadcastsRepository)
        `when`(broadcastsRepository.findPaginated(any())).thenReturn(pageDto)

        val participant = Participant.builder().id(participantId).build()

        `when`(repositoryFactory.getParticipantRepository(any())).thenReturn(participantRepository)
        `when`(participantRepository.findById(participantId)).thenReturn(participant)

        `when`(presenterFactory.getPresenter(any())).thenReturn(UIPresenter(null, clock))

        val searchParameters = BroadcastsSearchParameters()

        facadeImpl.getPaginated(PRODUCT, CLIENT_TYPE, searchParameters)

        verify(participantRepository).findById(participantId)
    }

    @Test
    fun `should create new broadcast`() {
        val participantId = "00000001"

        val broadcastDto = Broadcast(ZonedDateTime.parse("2021-01-22T00:00:00Z"), "id", "msg", listOf(participantId))

        `when`(repositoryFactory.getBroadcastsRepository(any())).thenReturn(broadcastsRepository)
        `when`(broadcastsRepository.create(any(), any())).thenReturn(broadcastDto)

        val participant = Participant.builder().id(participantId).build()

        `when`(repositoryFactory.getParticipantRepository(any())).thenReturn(participantRepository)
        `when`(participantRepository.findById(participantId)).thenReturn(participant)

        `when`(presenterFactory.getPresenter(any())).thenReturn(UIPresenter(null, clock))

        val created = facadeImpl.create(PRODUCT, CLIENT_TYPE, "msg", listOf(participantId))

        assertThat(created).isNotNull
        assertThat(created.recipients[0].participantIdentifier).isEqualTo(participantId)

        verify(participantRepository).findById(participantId)
    }

}