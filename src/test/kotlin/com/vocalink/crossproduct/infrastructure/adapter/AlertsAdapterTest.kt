package com.vocalink.crossproduct.infrastructure.adapter

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.adapter.bps.alert.BPSAlertsClient
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory
import com.vocalink.crossproduct.shared.alert.CPAlert
import com.vocalink.crossproduct.shared.alert.CPAlertReferenceData
import com.vocalink.crossproduct.shared.alert.CPAlertStats
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.Optional

class AlertsAdapterTest {

    private val clientFactory = mock(ClientFactory::class.java)!!
    private val client = mock(BPSAlertsClient::class.java)!!
    private val alertsAdapter = AlertsAdapter(clientFactory)

    @Test
    fun `should invoke get reference alerts client`() {
        val alertReferenceData = CPAlertReferenceData.builder().build()

        `when`(clientFactory.getAlertsClient(any()))
                .thenReturn(client)
        `when`(client.findReferenceAlerts())
                .thenReturn(Optional.of(alertReferenceData))

        alertsAdapter.findReferenceAlerts(TestConstants.CONTEXT)

        verify(clientFactory, atLeastOnce()).getAlertsClient(TestConstants.CONTEXT)
        verify(client, atLeastOnce()).findReferenceAlerts()
    }

    @Test
    fun `should invoke get alert stats client`() {
        val alertStats = CPAlertStats.builder().build()

        `when`(clientFactory.getAlertsClient(any()))
                .thenReturn(client)
        `when`(client.findAlertStats())
                .thenReturn(Optional.of(alertStats))

        alertsAdapter.findAlertStats(TestConstants.CONTEXT)

        verify(clientFactory, atLeastOnce()).getAlertsClient(TestConstants.CONTEXT)
        verify(client, atLeastOnce()).findAlertStats()
    }

    @Test
    fun `should invoke get alerts client`() {
        val alerts = listOf(CPAlert.builder().build())

        `when`(clientFactory.getAlertsClient(TestConstants.CONTEXT)).thenReturn(client)
        `when`(client.findAlerts(any())).thenReturn(alerts)

        alertsAdapter.findAlerts(TestConstants.CONTEXT, any())

        verify(clientFactory, atLeastOnce()).getAlertsClient(TestConstants.CONTEXT)
    }
}
