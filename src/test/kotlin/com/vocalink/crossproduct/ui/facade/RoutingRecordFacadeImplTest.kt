package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.routing.RoutingRecord
import com.vocalink.crossproduct.domain.routing.RoutingRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class RoutingRecordFacadeImplTest {

    private val routingRecordRepository = Mockito.mock(RoutingRepository::class.java)
    private val presenterFactory = Mockito.mock(PresenterFactory::class.java)!!
    private val uiPresenter = Mockito.mock(UIPresenter::class.java)!!
    private val repositoryFactory = Mockito.mock(RepositoryFactory::class.java)

    private val participantFacadeImpl = RoutingRecordFacadeImpl(
            repositoryFactory,
            presenterFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getRoutingRepository(anyString()))
                .thenReturn(routingRecordRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on routing records`() {
        val routingRecord = RoutingRecord(
                null, null, null, null
        )
        val routingRecordDto = RoutingRecordDto(
                null, null, null, null
        )
        val request = RoutingRecordRequest()

        `when`(routingRecordRepository.findPaginated(any()))
                .thenReturn(Page(1, listOf(routingRecord)))

        `when`(uiPresenter.presentRoutingRecords(any()))
                .thenReturn(PageDto(1, listOf((routingRecordDto))))

        val result = participantFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request,"")

        verify(uiPresenter).presentRoutingRecords(any())
        verify(presenterFactory).getPresenter(any())
        verify(routingRecordRepository).findPaginated(any())
        assertNotNull(result)
    }
}