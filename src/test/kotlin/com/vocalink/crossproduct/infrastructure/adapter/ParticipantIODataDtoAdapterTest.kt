package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.io.BPSParticipantIODataClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.mocks.MockIOData
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDate
import kotlin.test.assertEquals

class ParticipantIODataDtoAdapterTest {

    private val clientFactory = mock(ClientFactory::class.java)!!
    private val participantIOClient = mock(BPSParticipantIODataClient::class.java)!!
    private val participantIODataAdapter = ParticipantIODataAdapter(clientFactory)

    @Test
    fun `should find by given timestamp`() {
        val date = LocalDate.now()
        `when`(clientFactory.getParticipantIODataClient(TestConstants.CONTEXT))
                .thenReturn(participantIOClient)
        `when`(participantIOClient.findByTimestamp(date))
                .thenReturn(MockIOData().cpParticipantIOData)

        val result = participantIODataAdapter.findByTimestamp(TestConstants.CONTEXT, date)

        assertEquals(3, result.size)

        assertEquals("ESSESESS", result[0].participantId)
        assertEquals(1.00, result[0].batches.rejected)
        assertEquals(1, result[0].batches.submitted)
        assertEquals(1.00, result[0].files.rejected)
        assertEquals(1, result[0].files.submitted)
        assertEquals(1.00, result[0].transactions.rejected)
        assertEquals(1, result[0].transactions.submitted)

        assertEquals("HANDSESS", result[1].participantId)
        assertEquals(1.00, result[1].batches.rejected)
        assertEquals(10, result[1].batches.submitted)
        assertEquals(1.00, result[1].files.rejected)
        assertEquals(10, result[1].files.submitted)
        assertEquals(1.00, result[1].transactions.rejected)
        assertEquals(10, result[1].transactions.submitted)

        assertEquals("NDEASESSXXX", result[2].participantId)
        assertEquals(0.00, result[2].batches.rejected)
        assertEquals(0, result[2].batches.submitted)
        assertEquals(0.00, result[2].files.rejected)
        assertEquals(0, result[2].files.submitted)
        assertEquals(0.00, result[2].transactions.rejected)
        assertEquals(0, result[2].transactions.submitted)
    }
}