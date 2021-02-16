package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.report.Report
import com.vocalink.crossproduct.domain.report.ReportRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.report.ReportDto
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.assertNotNull

class ReportFacadeImplTest {

    private val reportRepository = mock(ReportRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val reportFacadeImpl = ReportFacadeImpl(
        repositoryFactory,
        presenterFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getReportRepository(anyString()))
            .thenReturn(reportRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
            .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get reports`() {
        val page = Page<Report>(1, listOf(Report(
            null, null, null, null, null, null)))
        val pageDto = PageDto<ReportDto>(1, listOf(ReportDto(
            null, null, null, null, null, null)))
        val request = ReportsSearchRequest()

        `when`(reportRepository.findPaginated(any())).thenReturn(page)

        `when`(uiPresenter.presentReports(any())).thenReturn(pageDto)

        val result = reportFacadeImpl.getPaginated(CONTEXT, ClientType.UI, request)

        verify(reportRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentReports(any())

        assertNotNull(result)
    }
}