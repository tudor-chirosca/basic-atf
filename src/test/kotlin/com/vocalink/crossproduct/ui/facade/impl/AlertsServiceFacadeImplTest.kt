package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.alert.AlertReferenceData
import com.vocalink.crossproduct.repository.AlertsRepository
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
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
}