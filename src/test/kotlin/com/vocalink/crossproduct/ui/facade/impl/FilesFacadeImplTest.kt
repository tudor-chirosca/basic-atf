package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.files.FileDetails
import com.vocalink.crossproduct.domain.files.FileEnquiry
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.repository.FileRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileEnquiryDto
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.Optional
import kotlin.test.assertNotNull

class FilesFacadeImplTest {

    private val fileRepository = mock(FileRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!

    private val filesServiceFacadeImpl = FilesFacadeImpl(
            presenterFactory,
            fileRepository
    )

    @Test
    fun `should invoke presenter and repository on get files`() {
        val page = Page<FileEnquiry>(1, listOf(FileEnquiry.builder().build()))
        val pageDto = PageDto<FileEnquiryDto>(1, listOf(FileEnquiryDto.builder().build()))
        val request = FileEnquirySearchRequest()

        `when`(fileRepository.findFileEnquiries(any(), any()))
                .thenReturn(page)

        `when`(presenterFactory.getPresenter(any()))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentEnquiries(any()))
                .thenReturn(pageDto)

        val result = filesServiceFacadeImpl.getFileEnquiries(TestConstants.CONTEXT, ClientType.UI, request)

        verify(fileRepository).findFileEnquiries(any(), any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentEnquiries(any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get file details`() {
        val details = FileDetails.builder().build()
        val detailsDto = FileDetailsDto.builder().build()

        `when`(fileRepository
                .findDetailsBy(any(), any()))
                .thenReturn(listOf(details))

        `when`(presenterFactory.getPresenter(any()))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentFileDetails(any()))
                .thenReturn(detailsDto)

        val result = filesServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(fileRepository).findDetailsBy(any(), any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentFileDetails(any())

        assertNotNull(result)
    }

    @Test
    fun `should throw error Not Found if no file details found`() {
        `when`(fileRepository.findDetailsBy(any(), any()))
                .thenReturn(emptyList())

        assertThrows(EntityNotFoundException::class.java) {
            filesServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")
        }
    }
}