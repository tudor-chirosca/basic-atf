package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.repository.AlertsRepository
import com.vocalink.crossproduct.shared.alert.AlertRequest
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.alert.AlertFilterRequest
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Optional
import kotlin.test.assertNotNull

@ExtendWith(SpringExtension::class)
class AlertsServiceFacadeImplTest {

    private val alertsRepository = Mockito.mock(AlertsRepository::class.java)!!
    private val presenterFactory = Mockito.mock(PresenterFactory::class.java)!!
    private val uiPresenter = Mockito.mock(UIPresenter::class.java)!!

    private val testingModule = AlertsServiceFacadeImpl(
            alertsRepository,
            presenterFactory
    )

    @Test
    fun `should get alerts reference`() {
        val alertReferenceData = AlertReferenceData.builder().build()
        val alertReferenceDataDto = AlertReferenceDataDto.builder().build()

        Mockito.`when`(alertsRepository
                .findReferenceAlerts(TestConstants.CONTEXT))
                .thenReturn(Optional.of(alertReferenceData))

        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        Mockito.`when`(uiPresenter.presentAlertReference(any()))
                .thenReturn(alertReferenceDataDto)

        val result = testingModule.getAlertsReference(TestConstants.CONTEXT, ClientType.UI)

        Mockito.verify(alertsRepository).findReferenceAlerts(any())
        Mockito.verify(presenterFactory).getPresenter(any())
        Mockito.verify(uiPresenter).presentAlertReference(any())

        assertNotNull(result)
    }

    @Test
    fun `should get alerts stats`() {
        val alertStats = AlertStats.builder().build()
        val alertStatsDto = AlertStatsDto.builder().build()

        Mockito.`when`(alertsRepository
                .findAlertStats(TestConstants.CONTEXT))
                .thenReturn(Optional.of(alertStats))

        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        Mockito.`when`(uiPresenter.presentAlertStats(any()))
                .thenReturn(alertStatsDto)

        val result = testingModule.getAlertStats(TestConstants.CONTEXT, ClientType.UI)

        Mockito.verify(alertsRepository).findAlertStats(any())
        Mockito.verify(presenterFactory).getPresenter(any())
        Mockito.verify(uiPresenter).presentAlertStats(any())

        assertNotNull(result)
    }

    @Test
    fun `should get alerts`() {
        val alertFilterRequest = AlertFilterRequest()
        val alertRequest = AlertRequest()
        val alerts = listOf(Alert())
        val alertsDto = listOf(AlertDto())

        Mockito.`when`(alertsRepository.findAlerts(TestConstants.CONTEXT, alertRequest)).thenReturn(alerts)
        Mockito.`when`(presenterFactory.getPresenter(ClientType.UI)).thenReturn(uiPresenter)
        Mockito.`when`(uiPresenter.presentAlert(alerts)).thenReturn(alertsDto)

        val result = testingModule.getAlerts(TestConstants.CONTEXT, ClientType.UI, alertFilterRequest)

        Mockito.verify(alertsRepository, Mockito.atLeastOnce()).findAlerts(any(), any())
        Mockito.verify(presenterFactory, Mockito.atLeastOnce()).getPresenter(any())
        Mockito.verify(uiPresenter, Mockito.atLeastOnce()).presentAlert(any())

        assertNotNull(result)
    }

    @Test
    fun `should throw error Not Found if no alert references found`() {
        Mockito.`when`(alertsRepository
                .findReferenceAlerts(TestConstants.CONTEXT))
                .thenReturn(Optional.empty())

        Assertions.assertThrows(EntityNotFoundException::class.java) {
            testingModule.getAlertsReference(TestConstants.CONTEXT, ClientType.UI)
        }
    }

    @Test
    fun `should throw error Not Found if no alert stats found`() {
        Mockito.`when`(alertsRepository
                .findAlertStats(TestConstants.CONTEXT))
                .thenReturn(Optional.empty())

        Assertions.assertThrows(EntityNotFoundException::class.java) {
            testingModule.getAlertStats(TestConstants.CONTEXT, ClientType.UI)
        }
    }
}