package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.alert.Alert
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.domain.alert.AlertRepository
import com.vocalink.crossproduct.domain.alert.AlertStats
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.alert.AlertDto
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertNotNull

class AlertsServiceFacadeImplTest {

    private val alertsRepository = mock(AlertRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val alertsServiceFacadeImpl = AlertsServiceFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getAlertsRepository(anyString()))
                .thenReturn(alertsRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should get alerts reference`() {
        val alertReferenceData = AlertReferenceData(null, null)
        val alertReferenceDataDto = AlertReferenceDataDto(null, null)

        `when`(alertsRepository.findAlertsReferenceData())
                .thenReturn(alertReferenceData)

        `when`(uiPresenter.presentAlertReference(any()))
                .thenReturn(alertReferenceDataDto)

        val result = alertsServiceFacadeImpl.getAlertsReference(TestConstants.CONTEXT, ClientType.UI)

        verify(alertsRepository).findAlertsReferenceData()
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentAlertReference(any())

        assertNotNull(result)
    }

    @Test
    fun `should get alerts stats`() {
        val alertStats = AlertStats(0, null)
        val alertStatsDto = AlertStatsDto(0, null)

        `when`(alertsRepository.findAlertStats())
                .thenReturn(alertStats)

        `when`(uiPresenter.presentAlertStats(any()))
                .thenReturn(alertStatsDto)

        val result = alertsServiceFacadeImpl.getAlertStats(TestConstants.CONTEXT, ClientType.UI)

        verify(alertsRepository).findAlertStats()
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentAlertStats(any())

        assertNotNull(result)
    }

    @Test
    fun `should get alerts`() {
        val alerts = Page<Alert>(2, listOf())
        val alertsDto = PageDto<AlertDto>(2, listOf<AlertDto>())

        val searchRequest = AlertSearchRequest()
        `when`(alertsRepository.findPaginated(any())).thenReturn(alerts)
        `when`(uiPresenter.presentAlert(alerts)).thenReturn(alertsDto)

        val result = alertsServiceFacadeImpl.getAlerts(TestConstants.CONTEXT, ClientType.UI, searchRequest)

        verify(alertsRepository, atLeastOnce()).findPaginated(any())
        verify(presenterFactory, atLeastOnce()).getPresenter(any())
        verify(uiPresenter, atLeastOnce()).presentAlert(any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get alerts`() {
        val page = Page<Alert>(1, listOf(Alert.builder().build()))
        val pageDto = PageDto<AlertDto>(1, listOf(AlertDto.builder().build()))
        val request = AlertSearchRequest()

        `when`(alertsRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(uiPresenter.presentAlert(any()))
                .thenReturn(pageDto)

        val result = alertsServiceFacadeImpl.getAlerts(TestConstants.CONTEXT, ClientType.UI, request)

        verify(alertsRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentAlert(any())

        assertNotNull(result)
    }
}
