package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.repository.AlertsRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.Optional
import kotlin.test.assertNotNull

class AlertsServiceFacadeImplTest {

    private val alertsRepository = mock(AlertsRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val alertsServiceFacadeImpl = AlertsServiceFacadeImpl(
            alertsRepository,
            presenterFactory
    )

    @Test
    fun `should get alerts reference`() {
        val alertReferenceData = AlertReferenceData.builder().build()
        val alertReferenceDataDto = AlertReferenceDataDto.builder().build()

        `when`(alertsRepository
                .findReferenceAlerts(TestConstants.CONTEXT))
                .thenReturn(Optional.of(alertReferenceData))

        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentAlertReference(any()))
                .thenReturn(alertReferenceDataDto)

        val result = alertsServiceFacadeImpl.getAlertsReference(TestConstants.CONTEXT, ClientType.UI)

        verify(alertsRepository).findReferenceAlerts(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentAlertReference(any())

        assertNotNull(result)
    }

    @Test
    fun `should get alerts stats`() {
        val alertStats = AlertStats.builder().build()
        val alertStatsDto = AlertStatsDto.builder().build()

        `when`(alertsRepository
                .findAlertStats(TestConstants.CONTEXT))
                .thenReturn(Optional.of(alertStats))

        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentAlertStats(any()))
                .thenReturn(alertStatsDto)

        val result = alertsServiceFacadeImpl.getAlertStats(TestConstants.CONTEXT, ClientType.UI)

        verify(alertsRepository).findAlertStats(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentAlertStats(any())

        assertNotNull(result)
    }

    @Test
    fun `should get alerts`() {
        val searchRequest = AlertSearchRequest(null,null,null,null,null,null,null,null,null,null)
        val alerts = Page<Alert>(2, listOf())
        val alertsDto = PageDto(2, listOf<AlertDto>())

        `when`(alertsRepository.findAlerts(TestConstants.CONTEXT, searchRequest)).thenReturn(alerts)
        `when`(presenterFactory.getPresenter(ClientType.UI)).thenReturn(uiPresenter)
        `when`(uiPresenter.presentAlert(alerts)).thenReturn(alertsDto)

        val result = alertsServiceFacadeImpl.getAlerts(TestConstants.CONTEXT, ClientType.UI, searchRequest)

        verify(alertsRepository, atLeastOnce()).findAlerts(any(), any())
        verify(presenterFactory, atLeastOnce()).getPresenter(any())
        verify(uiPresenter, atLeastOnce()).presentAlert(any())

        assertNotNull(result)
    }

    @Test
    fun `should throw error Not Found if no alert references found`() {
        `when`(alertsRepository
                .findReferenceAlerts(TestConstants.CONTEXT))
                .thenReturn(Optional.empty())

        Assertions.assertThrows(EntityNotFoundException::class.java) {
            alertsServiceFacadeImpl.getAlertsReference(TestConstants.CONTEXT, ClientType.UI)
        }
    }

    @Test
    fun `should throw error Not Found if no alert stats found`() {
        `when`(alertsRepository
                .findAlertStats(TestConstants.CONTEXT))
                .thenReturn(Optional.empty())

        Assertions.assertThrows(EntityNotFoundException::class.java) {
            alertsServiceFacadeImpl.getAlertStats(TestConstants.CONTEXT, ClientType.UI)
        }
    }
}