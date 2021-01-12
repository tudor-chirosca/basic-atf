package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.batch.Batch
import com.vocalink.crossproduct.domain.batch.BatchRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto
import com.vocalink.crossproduct.ui.dto.batch.BatchDto
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class BatchesFacadeImplTest {

    private val batchRepository = mock(BatchRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val batchesServiceFacadeImpl = BatchesFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getBatchRepository(anyString()))
                .thenReturn(batchRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get batches`() {
        val page = Page<Batch>(1, listOf(Batch.builder().build()))
        val pageDto = PageDto<BatchDto>(1, listOf(BatchDto.builder().build()))
        val request = BatchEnquirySearchRequest()

        `when`(batchRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(uiPresenter.presentBatches(any()))
                .thenReturn(pageDto)

        val result = batchesServiceFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(batchRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentBatches(any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get batch details`() {
        val batch = Batch.builder().build()
        val batchDetailsDto = BatchDetailsDto.builder().build()

        `when`(batchRepository
                .findById(any()))
                .thenReturn(batch)

        `when`(uiPresenter.presentBatchDetails(any()))
                .thenReturn(batchDetailsDto)

        val result = batchesServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(batchRepository).findById(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentBatchDetails(any())

        assertNotNull(result)
    }
}