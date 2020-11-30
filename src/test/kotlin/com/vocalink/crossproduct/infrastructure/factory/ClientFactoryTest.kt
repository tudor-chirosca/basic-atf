package com.vocalink.crossproduct.infrastructure.factory

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.alert.BPSAlertsClient
import com.vocalink.crossproduct.adapter.bps.batch.BPSBatchesClient
import com.vocalink.crossproduct.adapter.bps.cycle.BPSCyclesClient
import com.vocalink.crossproduct.adapter.bps.files.BPSFilesClient
import com.vocalink.crossproduct.adapter.bps.io.BPSParticipantIODataClient
import com.vocalink.crossproduct.adapter.bps.participant.BPSParticipantClient
import com.vocalink.crossproduct.adapter.bps.positions.BPSPositionClient
import com.vocalink.crossproduct.adapter.bps.reference.BPSReferencesClient
import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException
import com.vocalink.crossproduct.shared.alert.AlertsClient
import com.vocalink.crossproduct.shared.batch.BatchesClient
import com.vocalink.crossproduct.shared.cycle.CyclesClient
import com.vocalink.crossproduct.shared.files.FilesClient
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient
import com.vocalink.crossproduct.shared.participant.ParticipantClient
import com.vocalink.crossproduct.shared.positions.PositionClient
import com.vocalink.crossproduct.shared.reference.ReferencesClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertTrue

class ClientFactoryTest {

    private val cyclesClient: CyclesClient = mock(BPSCyclesClient::class.java)!!
    private val participantIOClient: ParticipantIODataClient = mock(BPSParticipantIODataClient::class.java)!!
    private val participantClient: ParticipantClient = mock(BPSParticipantClient::class.java)!!
    private val positionClient: PositionClient = mock(BPSPositionClient::class.java)!!
    private val alertsClient: AlertsClient = mock(BPSAlertsClient::class.java)!!
    private val filesClient: FilesClient = mock(BPSFilesClient::class.java)!!
    private val referencesClient: ReferencesClient = mock(BPSReferencesClient::class.java)!!
    private val batchesClient: BatchesClient = mock(BPSBatchesClient::class.java)!!

    private val clientFactory: ClientFactory = ClientFactory(
            listOf(participantClient),
            listOf(cyclesClient),
            listOf(participantIOClient),
            listOf(positionClient),
            listOf(alertsClient),
            listOf(filesClient),
            listOf(referencesClient),
            listOf(batchesClient)
    )

    @Test
    fun `should get participant client`() {
        `when`(participantClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getParticipantClient(TestConstants.CONTEXT)
        assertTrue(result is ParticipantClient)
    }

    @Test
    fun `should get cycles client`() {
        `when`(cyclesClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getCyclesClient(TestConstants.CONTEXT)
        assertTrue(result is CyclesClient)
    }

    @Test
    fun `should get participant IO client`() {
        `when`(participantIOClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getParticipantIODataClient(TestConstants.CONTEXT)
        assertTrue(result is ParticipantIODataClient)
    }

    @Test
    fun `should get positions client`() {
        `when`(positionClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getPositionClient(TestConstants.CONTEXT)
        assertTrue(result is PositionClient)
    }

    @Test
    fun `should get alerts client`() {
        `when`(alertsClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getAlertsClient(TestConstants.CONTEXT)
        assertTrue(result is AlertsClient)
    }

    @Test
    fun `should get reference client`() {
        `when`(referencesClient.context).thenReturn(TestConstants.CONTEXT)
        clientFactory.init()

        val result = clientFactory.getReferencesClient(TestConstants.CONTEXT)
        assertTrue(result is ReferencesClient)
    }

    @Test
    fun `should throw participant client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getParticipantClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw cycles client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getCyclesClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw participant IO client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getParticipantIODataClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw position client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getPositionClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw alerts client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getAlertsClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw flies client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getFilesClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw references client not available for wrong context`() {
        clientFactory.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            clientFactory.getReferencesClient(TestConstants.CONTEXT)
        }
    }
}