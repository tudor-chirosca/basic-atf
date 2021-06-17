package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.ServiceFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.ResourceService
import com.vocalink.crossproduct.domain.files.File
import com.vocalink.crossproduct.domain.files.FileRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.file.FileDetailsDto
import com.vocalink.crossproduct.ui.dto.file.FileDto
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest
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

class FilesFacadeImplTest {

    private val fileRepository = mock(FileRepository::class.java)!!
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val downloadService = mock(ResourceService::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val serviceFactory = mock(ServiceFactory::class.java)

    private val filesServiceFacadeImpl = FilesFacadeImpl(
            presenterFactory,
            repositoryFactory,
            serviceFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getFileRepository(anyString()))
                .thenReturn(fileRepository)
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(serviceFactory.getDownloadService(anyString()))
                .thenReturn(downloadService)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get files`() {
        val resultFile = Page<File>(1, listOf(File.builder().build()))
        val pageDto = PageDto<FileDto>(1, listOf(FileDto.builder().build()))
        val request = FileEnquirySearchRequest()

        `when`(fileRepository.findBy(any())).thenReturn(resultFile)
        `when`(uiPresenter.presentFiles(any(), any())).thenReturn(pageDto)

        val result = filesServiceFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(fileRepository).findBy(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentFiles(any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on get file details`() {
        val file = File.builder().from("any").build()
        val detailsDto = FileDetailsDto.builder().build()
        val participant = Participant.builder().build()

        `when`(fileRepository.findById(any())).thenReturn(file)

        `when`(participantRepository.findById(anyString())).thenReturn(participant)

        `when`(uiPresenter.presentFileDetails(any(), any()))
                .thenReturn(detailsDto)

        val result = filesServiceFacadeImpl.getDetailsById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(fileRepository).findById(any())
        verify(participantRepository).findById(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentFileDetails(any(), any())

        assertNotNull(result)
    }
}
