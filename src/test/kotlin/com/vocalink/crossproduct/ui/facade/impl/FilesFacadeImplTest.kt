package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.domain.files.FileRepository
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import kotlin.test.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

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
        val page = Page<File>(1, listOf(File.builder().build()))
        val pageDto = PageDto<FileDto>(1, listOf(FileDto.builder().build()))
        val request = FileEnquirySearchRequest()

        `when`(fileRepository.findFilesPaginated(any(), any()))
                .thenReturn(page)

        `when`(presenterFactory.getPresenter(any()))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentFiles(any()))
                .thenReturn(pageDto)

        val result = filesServiceFacadeImpl.getFiles(TestConstants.CONTEXT, ClientType.UI, request)

        verify(fileRepository).findFilesPaginated(any(), any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentFiles(any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get file details`() {
        val details = File.builder().build()
        val detailsDto = FileDetailsDto.builder().build()

        `when`(fileRepository
                .findFilesByIds(any(), any()))
                .thenReturn(listOf(details))

        `when`(presenterFactory.getPresenter(any()))
                .thenReturn(uiPresenter)

        `when`(uiPresenter.presentFileDetails(any()))
                .thenReturn(detailsDto)

        val result = filesServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(fileRepository).findFilesByIds(any(), any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentFileDetails(any())

        assertNotNull(result)
    }

    @Test
    fun `should throw error Not Found if no file details found`() {
        `when`(fileRepository.findFilesByIds(any(), any()))
                .thenReturn(emptyList())

        assertThrows(EntityNotFoundException::class.java) {
            filesServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")
        }
    }
}
