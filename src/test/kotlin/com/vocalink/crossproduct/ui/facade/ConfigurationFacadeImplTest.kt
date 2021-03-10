package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.ServiceFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.configuration.Configuration
import com.vocalink.crossproduct.domain.configuration.ConfigurationService
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto
import com.vocalink.crossproduct.ui.presenter.ClientType.UI
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.lang.Integer.parseInt

class ConfigurationFacadeImplTest {

    private val configurationService = mock(ConfigurationService::class.java)

    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val serviceFactory = mock(ServiceFactory::class.java)

    private val configurationFacadeImpl = ConfigurationFacadeImpl(
        presenterFactory,
        serviceFactory
    )

    @BeforeEach
    fun init() {
        `when`(serviceFactory.getConfigurationService(anyString()))
            .thenReturn(configurationService)
        `when`(presenterFactory.getPresenter(UI))
            .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke configuration service and presenter`() {
        val scheme = "P27-SEK"
        val schemeCurrency = "SEK"
        val dataRetentionDays = parseInt(getDefault(DAYS_LIMIT))

        val configuration = Configuration(scheme, schemeCurrency)
        val configurationDto = ConfigurationDto(scheme, schemeCurrency, dataRetentionDays)

        `when`(configurationService.configuration)
            .thenReturn(configuration)
        `when`(uiPresenter.presentConfiguration(configuration, dataRetentionDays))
            .thenReturn(configurationDto)

        val result = configurationFacadeImpl.getConfiguration(CONTEXT, UI)

        verify(configurationService).configuration
        verify(uiPresenter).presentConfiguration(configuration, dataRetentionDays)

        assertThat(result.dataRetentionDays).isEqualTo(parseInt(getDefault(DAYS_LIMIT)))
        assertThat(result.scheme).isEqualTo(scheme)
        assertThat(result.schemeCurrency).isEqualTo(schemeCurrency)
    }
}