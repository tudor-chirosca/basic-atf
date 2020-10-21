package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.adapter.bps.participant.BPSParticipantClient
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.infrastructure.config.AppConfig
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.mocks.MockParticipants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ParticipantRepositoryAdapterTest {
    private val clientFactory = Mockito.mock(ClientFactory::class.java)!!
    private var participantClient = Mockito.mock(BPSParticipantClient::class.java)!!
    private val modelMapper = AppConfig().modelMapper()

    private var testingModule = ParticipantRepositoryAdapter(clientFactory)

    @BeforeEach
    fun setup() {
        testingModule.setModelMapper(modelMapper)
    }

    @Test
    fun `should find all participants`() {
        Mockito.`when`(clientFactory.getParticipantClient(CONTEXT))
                .thenReturn(participantClient)
        Mockito.`when`(participantClient.findAll())
                .thenReturn(MockParticipants().cpParticipants)

        val result = testingModule.findAll(CONTEXT)

        assertEquals(result.size, 3)
        assertTrue(result[0] is Participant)

        assertEquals("HANDSESS", result[0].bic)
        assertEquals("HANDSESS", result[0].id)
        assertEquals("Svenska Handelsbanken", result[0].name)
        assertEquals(ParticipantStatus.SUSPENDED, result[0].status)
        assertNotNull(result[0].suspendedTime)

        assertEquals("NDEASESSXXX", result[1].bic)
        assertEquals("NDEASESSXXX", result[1].id)
        assertEquals("Nordea", result[1].name)
        assertEquals(ParticipantStatus.ACTIVE, result[1].status)
        assertNull(result[1].suspendedTime)

        assertEquals("ESSESESS", result[2].bic)
        assertEquals("ESSESESS", result[2].id)
        assertEquals("SEB Bank", result[2].name)
        assertEquals(ParticipantStatus.ACTIVE, result[2].status)
        assertNull(result[2].suspendedTime)
    }

    @Test
    fun `should find participants by id`() {
        val participantId = "HANDSESS"

        Mockito.`when`(clientFactory.getParticipantClient(CONTEXT))
                .thenReturn(participantClient)
        Mockito.`when`(participantClient.findById(participantId))
                .thenReturn(MockParticipants().cpParticipants)

        val optionalResult = testingModule.findByParticipantId(CONTEXT, participantId)

        assertTrue(optionalResult.isPresent)

        val unwrappedResult = optionalResult.get()

        assertTrue(unwrappedResult is Participant)
        assertEquals("HANDSESS", unwrappedResult.bic)
        assertEquals("HANDSESS", unwrappedResult.id)
        assertEquals("Svenska Handelsbanken", unwrappedResult.name)
        assertEquals(ParticipantStatus.SUSPENDED, unwrappedResult.status)
        assertNotNull(unwrappedResult.suspendedTime)
    }

    @Test
    fun `should return empty optional when no participant for given id`() {
        val participantId = "HANDSESS"

        Mockito.`when`(clientFactory.getParticipantClient(CONTEXT))
                .thenReturn(participantClient)
        Mockito.`when`(participantClient.findById(participantId)).thenReturn(emptyList())

        val optionalResult = testingModule.findByParticipantId(CONTEXT, participantId)

        assertFalse(optionalResult.isPresent)
    }

}