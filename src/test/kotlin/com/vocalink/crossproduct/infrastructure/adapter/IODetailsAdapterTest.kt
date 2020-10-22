package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.io.BPSParticipantIODataClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.mocks.MockIOData
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDate
import kotlin.test.assertEquals

class IODetailsAdapterTest {

    private val clientFactory = Mockito.mock(ClientFactory::class.java)!!
    private var client = Mockito.mock(BPSParticipantIODataClient::class.java)!!
    private var testingModule = IODetailsAdapter(clientFactory)

    @Test
    fun `should find by given timestamp`() {
        val participantId = "NDEASESSXXX"
        val date = LocalDate.now()
        Mockito.`when`(clientFactory.getParticipantIODataClient(TestConstants.CONTEXT))
                .thenReturn(client)
        Mockito.`when`(client.findIODetailsFor(participantId, date))
                .thenReturn(MockIOData().getCPIODetails())

        val result = testingModule.findIODetailsFor(TestConstants.CONTEXT, participantId, date)

        assertEquals(3, result[0].batches.size)
        assertEquals("Pacs.008", result[0].batches[0].code)
        assertEquals("Customer Credit Transfer", result[0].batches[0].name)

        assertEquals(10, result[0].files.submitted)
        assertEquals(10, result[0].files.accepted)
        assertEquals(1.5, result[0].files.rejected)
        assertEquals(10, result[0].files.output)

        assertEquals(10, result[0].batches[0].data.accepted)
        assertEquals(10, result[0].batches[0].data.output)
        assertEquals(1.50, result[0].batches[0].data.rejected)
        assertEquals(10, result[0].batches[0].data.submitted)

        assertEquals(3, result[0].transactions.size)
        assertEquals("Pacs.004", result[0].transactions[1].code)
        assertEquals("Payment Return", result[0].transactions[1].name)
        assertEquals(10, result[0].transactions[1].data.accepted)
        assertEquals(10, result[0].transactions[1].data.output)
        assertEquals(1.50, result[0].transactions[1].data.rejected)
        assertEquals(10, result[0].transactions[1].data.submitted)
        assertEquals(10, result[0].transactions[1].data.amountAccepted)
        assertEquals(10, result[0].transactions[1].data.amountOutput)
    }
}