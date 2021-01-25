package com.vocalink.crossproduct.ui.facade.impl

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.approval.ApprovalDetails
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.approval.ApprovalRepository
import com.vocalink.crossproduct.domain.approval.ApprovalUser
import com.vocalink.crossproduct.domain.approval.RejectionReason
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat

class ApprovalFacadeImplTest {

    private val approvalRepository = mock(ApprovalRepository::class.java)!!
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val approvalFacadeImpl = ApprovalFacadeImpl(
            presenterFactory,
            repositoryFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getApprovalClient(ArgumentMatchers.anyString()))
                .thenReturn(approvalRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get approval details`() {
        val approvalUser = ApprovalUser("John Doe", "12a514")
        val jobId = "10000020"
        val createdAt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+1"))
        val rejectionReason = RejectionReason(approvalUser,
                "Please check ticket number...")
        val requestedChange = mapOf("status" to "Suspended")

        val approvalDetails = ApprovalDetails(
                ApprovalStatus.APPROVED,
                approvalUser,approvalUser,
                createdAt, jobId,
                ApprovalRequestType.UNSUSPEND,
                "FORXSES1",
                "Forex Bank",
                rejectionReason,
                requestedChange)

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                createdAt, jobId,
                ApprovalRequestType.UNSUSPEND,
                "FORXSES1",
                "Forex Bank",
                rejectionReason,
                requestedChange)

        `when`(approvalRepository.findByJobId(jobId))
                .thenReturn(approvalDetails)

        `when`(uiPresenter.presentApprovalDetails(any()))
                .thenReturn(approvalDetailsDto)

        val result = approvalFacadeImpl.getApprovalDetailsById(TestConstants.CONTEXT, ClientType.UI, jobId)

        verify(approvalRepository).findByJobId(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentApprovalDetails(any())

        assertThat(result).isNotNull
    }
}
