package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.ServiceFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.validation.ValidationApproval
import com.vocalink.crossproduct.domain.validation.ValidationService
import com.vocalink.crossproduct.ui.dto.validation.ValidationApprovalDto
import com.vocalink.crossproduct.ui.presenter.ClientType.UI
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import java.time.ZoneId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.ZonedDateTime

class ValidationFacadeImplTest {

    private val validationService = mock(ValidationService::class.java)

    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val serviceFactory = mock(ServiceFactory::class.java)

    private val validationFacadeImpl = ValidationFacadeImpl(
            presenterFactory,
            serviceFactory
    )

    @BeforeEach
    fun init() {
        `when`(serviceFactory.getValidationService(ArgumentMatchers.anyString()))
                .thenReturn(validationService)
        `when`(presenterFactory.getPresenter(UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke validation service and presenter with current date`() {
        val currentDate = ZonedDateTime.now(ZoneId.of("UTC"))
        val validationApproval = ValidationApproval(true, currentDate)
        val validationApprovalDto = ValidationApprovalDto(true, currentDate)

        `when`(validationService.getApprovalValidation(currentDate))
                .thenReturn(validationApproval)
        `when`(uiPresenter.presentApprovalValidation(validationApproval))
                .thenReturn(validationApprovalDto)

        val result = validationFacadeImpl.getApprovalValidation(CONTEXT, UI, currentDate)

        verify(validationService).getApprovalValidation(currentDate)
        verify(uiPresenter).presentApprovalValidation(validationApproval)

        assertThat(result.isCanExecute).isEqualTo(true)
        assertThat(result.timestamp).isEqualTo(currentDate)
    }
}