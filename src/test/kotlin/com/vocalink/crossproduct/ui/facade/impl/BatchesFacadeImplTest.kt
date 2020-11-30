package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.repository.BatchRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class BatchesFacadeImplTest {

    private val batchRepository = mock(BatchRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val batchesServiceFacadeImpl = BatchesFacadeImpl(
            presenterFactory,
            batchRepository
    )

    @Test
    fun `should invoke presenter and repository on get batches`() {
        val page = Page<Batch>(1, listOf(Batch.builder().build()))
        val pageDto = PageDto<BatchDto>(1, listOf(BatchDto.builder().build()))
        val request = BatchEnquirySearchRequest()

        `when`(batchRepository.findBatches(any(), any()))
                .thenReturn(page)

        `when`(presenterFactory.getPresenter(any()))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentBatches(any()))
                .thenReturn(pageDto)

        val result = batchesServiceFacadeImpl.getBatches(TestConstants.CONTEXT, ClientType.UI, request)

        verify(batchRepository).findBatches(any(), any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentBatches(any())

        assertNotNull(result)
    }
}