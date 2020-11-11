package com.vocalink.crossproduct.ui.presenter

import com.vocalink.crossproduct.infrastructure.exception.PresenterNotAvailableForClientTypeException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.test.assertTrue

class PresenterFactoryTests {

    private val presenter: Presenter = mock(UIPresenter::class.java)!!

    private val presenterFactory = PresenterFactory(
            listOf(presenter)
    )

    @Test
    fun `should get UI presenter`() {
        `when`(presenter.clientType).thenReturn(ClientType.UI)
        presenterFactory.init()

        val result = presenterFactory.getPresenter(ClientType.UI)
        assertTrue(result is UIPresenter)
    }

    @Test
    fun `should get SYSTEM presenter`() {
        `when`(presenter.clientType).thenReturn(ClientType.SYSTEM)
        presenterFactory.init()

        val result = presenterFactory.getPresenter(ClientType.SYSTEM)
        assertTrue(result is UIPresenter)
    }

    @Test
    fun `should throw Presenter Not Available For Client Exception`() {
        presenterFactory.init()
        Assertions.assertThrows(PresenterNotAvailableForClientTypeException::class.java) {
            presenterFactory.getPresenter(ClientType.UI)
        }
    }
}