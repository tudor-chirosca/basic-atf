package com.vocalink.crossproduct.infrastructure.factory

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.io.BPSParticipantIODataClient
import com.vocalink.crossproduct.adapter.bps.settlement.BPSSettlementsClient
import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient
import com.vocalink.crossproduct.shared.settlement.SettlementsClient
import kotlin.test.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ClientFactoryTest {

    private val participantIOClient: ParticipantIODataClient = mock(BPSParticipantIODataClient::class.java)!!
    private val settlementsClient: SettlementsClient = mock(BPSSettlementsClient::class.java)!!

    private val clientFactory: ClientFactory = ClientFactory(
            listOf(participantIOClient),
            listOf(settlementsClient)
    )

    @Test
    fun `should get participant IO client`() {
        `when`(participantIOClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getParticipantIODataClient(TestConstants.CONTEXT)
        assertTrue(result is ParticipantIODataClient)
    }

    @Test
    fun `should throw participant IO client not available for wrong context`() {
        clientFactory.init()
        assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getParticipantIODataClient(TestConstants.CONTEXT)
        }
    }

}
