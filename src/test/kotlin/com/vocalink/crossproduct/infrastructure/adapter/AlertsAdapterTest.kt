package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.alert.BPSAlertsClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.alert.CPAlertReferenceData
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import java.util.Optional

class AlertsAdapterTest {

    private val clientFactory = Mockito.mock(ClientFactory::class.java)!!
    private val client = Mockito.mock(BPSAlertsClient::class.java)!!
    private val testingModule = AlertsAdapter(clientFactory)

    @Test
    fun `should find by given timestamp`() {
        val alertReferenceData = CPAlertReferenceData.builder().build()

        Mockito.`when`(clientFactory.getAlertsClient(any()))
                .thenReturn(client)
        Mockito.`when`(client.findReferenceAlerts())
                .thenReturn(Optional.of(alertReferenceData))

        testingModule.findReferenceAlerts(TestConstants.CONTEXT)

        Mockito.verify(clientFactory, Mockito.atLeastOnce()).getAlertsClient(TestConstants.CONTEXT)
        Mockito.verify(client, Mockito.atLeastOnce()).findReferenceAlerts()
    }
}
