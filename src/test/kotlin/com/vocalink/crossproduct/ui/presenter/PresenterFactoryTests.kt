package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.infrastructure.exception.PresenterNotAvailableForClientTypeException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertTrue

@ExtendWith(SpringExtension::class)
class PresenterFactoryTests {

    private var presenter: Presenter = Mockito.mock(UIPresenter::class.java)!!

    private var testingModule = PresenterFactory(
            listOf(presenter)
    )

    @Test
    fun `should get UI presenter`() {
        Mockito.`when`(presenter.clientType).thenReturn(ClientType.UI)
        testingModule.init()

        val result = testingModule.getPresenter(ClientType.UI)
        assertTrue(result is UIPresenter)
    }

    @Test
    fun `should get SYSTEM presenter`() {
        Mockito.`when`(presenter.clientType).thenReturn(ClientType.SYSTEM)
        testingModule.init()

        val result = testingModule.getPresenter(ClientType.SYSTEM)
        assertTrue(result is UIPresenter)
    }

    @Test
    fun `should throw Presenter Not Available For Client Exception`() {
        testingModule.init()
        Assertions.assertThrows(PresenterNotAvailableForClientTypeException::class.java) {
            testingModule.getPresenter(ClientType.UI)
        }
    }
}