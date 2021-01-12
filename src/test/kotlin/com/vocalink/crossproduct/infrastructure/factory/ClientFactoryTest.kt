package com.vocalink.crossproduct.infrastructure.factory

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.alert.BPSAlertsClient
import com.vocalink.crossproduct.adapter.bps.batch.BPSBatchesClient
import com.vocalink.crossproduct.adapter.bps.files.BPSFilesClient
import com.vocalink.crossproduct.adapter.bps.io.BPSParticipantIODataClient
import com.vocalink.crossproduct.adapter.bps.reference.BPSReferencesClient
import com.vocalink.crossproduct.adapter.bps.settlement.BPSSettlementsClient
import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException
import com.vocalink.crossproduct.shared.alert.AlertsClient
import com.vocalink.crossproduct.shared.batch.BatchesClient
import com.vocalink.crossproduct.shared.files.FilesClient
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient
import com.vocalink.crossproduct.shared.reference.ReferencesClient
import com.vocalink.crossproduct.shared.settlement.SettlementsClient
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.test.assertTrue

class ClientFactoryTest {

    private val participantIOClient: ParticipantIODataClient = mock(BPSParticipantIODataClient::class.java)!!
    private val alertsClient: AlertsClient = mock(BPSAlertsClient::class.java)!!
    private val filesClient: FilesClient = mock(BPSFilesClient::class.java)!!
    private val referencesClient: ReferencesClient = mock(BPSReferencesClient::class.java)!!
    private val batchesClient: BatchesClient = mock(BPSBatchesClient::class.java)!!
    private val settlementsClient: SettlementsClient = mock(BPSSettlementsClient::class.java)!!

    private val clientFactory: ClientFactory = ClientFactory(
            listOf(participantIOClient),
            listOf(alertsClient),
            listOf(filesClient),
            listOf(batchesClient),
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
    fun `should get alerts client`() {
        `when`(alertsClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getAlertsClient(TestConstants.CONTEXT)
        assertTrue(result is AlertsClient)
    }

    @Test
    fun `should throw participant IO client not available for wrong context`() {
        clientFactory.init()
        assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getParticipantIODataClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw alerts client not available for wrong context`() {
        clientFactory.init()
        assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getAlertsClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw flies client not available for wrong context`() {
        clientFactory.init()
        assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getFilesClient(TestConstants.CONTEXT)
        }
    }
}
