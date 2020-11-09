package com.vocalink.crossproduct.infrastructure.factory

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.alert.BPSAlertsClient
import com.vocalink.crossproduct.adapter.bps.cycle.BPSCyclesClient
import com.vocalink.crossproduct.adapter.bps.files.BPSFilesClient
import com.vocalink.crossproduct.adapter.bps.io.BPSParticipantIODataClient
import com.vocalink.crossproduct.adapter.bps.participant.BPSParticipantClient
import com.vocalink.crossproduct.adapter.bps.positions.BPSPositionClient
import com.vocalink.crossproduct.adapter.bps.reference.BPSReferencesClient
import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException
import com.vocalink.crossproduct.shared.alert.AlertsClient
import com.vocalink.crossproduct.shared.cycle.CyclesClient
import com.vocalink.crossproduct.shared.files.FilesClient
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient
import com.vocalink.crossproduct.shared.participant.ParticipantClient
import com.vocalink.crossproduct.shared.positions.PositionClient
import com.vocalink.crossproduct.shared.reference.ReferencesClient
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class ClientFactoryTest {

    private var cyclesClient: CyclesClient = Mockito.mock(BPSCyclesClient::class.java)!!
    private var participantIOClient: ParticipantIODataClient = Mockito.mock(BPSParticipantIODataClient::class.java)!!
    private var participantClient: ParticipantClient = Mockito.mock(BPSParticipantClient::class.java)!!
    private var positionClient: PositionClient = Mockito.mock(BPSPositionClient::class.java)!!
    private var alertsClient: AlertsClient = Mockito.mock(BPSAlertsClient::class.java)!!
    private var filesClient: FilesClient = Mockito.mock(BPSFilesClient::class.java)!!
    private var referencesClient: ReferencesClient = Mockito.mock(BPSReferencesClient::class.java)!!

    private var testingModule: ClientFactory = ClientFactory(
            listOf(participantClient),
            listOf(cyclesClient),
            listOf(participantIOClient),
            listOf(positionClient),
            listOf(alertsClient),
            listOf(filesClient),
            listOf(referencesClient)
    )

    @Test
    fun `should get participant client`() {
        Mockito.`when`(participantClient.context).thenReturn(TestConstants.CONTEXT)
        testingModule.init()

        val result = testingModule.getParticipantClient(TestConstants.CONTEXT)
        assertTrue(result is ParticipantClient)
    }

    @Test
    fun `should get cycles client`() {
        Mockito.`when`(cyclesClient.context).thenReturn(TestConstants.CONTEXT)
        testingModule.init()

        val result = testingModule.getCyclesClient(TestConstants.CONTEXT)
        assertTrue(result is CyclesClient)
    }

    @Test
    fun `should get participant IO client`() {
        Mockito.`when`(participantIOClient.context).thenReturn(TestConstants.CONTEXT)
        testingModule.init()

        val result = testingModule.getParticipantIODataClient(TestConstants.CONTEXT)
        assertTrue(result is ParticipantIODataClient)
    }

    @Test
    fun `should get positions client`() {
        Mockito.`when`(positionClient.context).thenReturn(TestConstants.CONTEXT)
        testingModule.init()

        val result = testingModule.getPositionClient(TestConstants.CONTEXT)
        assertTrue(result is PositionClient)
    }

    @Test
    fun `should get alerts client`() {
        Mockito.`when`(alertsClient.context).thenReturn(TestConstants.CONTEXT)
        testingModule.init()

        val result = testingModule.getAlertsClient(TestConstants.CONTEXT)
        assertTrue(result is AlertsClient)
    }

    @Test
    fun `should throw participant client not available for wrong context`() {
        testingModule.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            testingModule.getParticipantClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw cycles client not available for wrong context`() {
        testingModule.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            testingModule.getCyclesClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw participant IO client not available for wrong context`() {
        testingModule.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            testingModule.getParticipantIODataClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw position client not available for wrong context`() {
        testingModule.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            testingModule.getPositionClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw alerts client not available for wrong context`() {
        testingModule.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            testingModule.getAlertsClient(TestConstants.CONTEXT)
        }
    }

    @Test
    fun `should throw flies client not available for wrong context`() {
        testingModule.init()
        Assertions.assertThrows(ClientNotAvailableException::class.java) {
            testingModule.getFilesClient(TestConstants.CONTEXT)
        }
    }
}