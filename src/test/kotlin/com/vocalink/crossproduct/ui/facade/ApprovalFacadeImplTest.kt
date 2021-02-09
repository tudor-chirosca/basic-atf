package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.approval.Approval
import com.vocalink.crossproduct.domain.approval.ApprovalRepository
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.approval.ApprovalUser
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

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
        `when`(repositoryFactory.getApprovalRepository(ArgumentMatchers.anyString()))
                .thenReturn(approvalRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get approval details`() {
        val approvalUser = ApprovalUser("John Doe", "12a514", "P27 Scheme")
        val approvalId = "10000020"
        val date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+1"))
        val requestedChange = mapOf("status" to "Suspended")
        val originalData = mapOf("data" to "data")

        val approvalDetails = Approval(
                approvalId,
                ApprovalRequestType.STATUS_CHANGE,
                "FORXSES1",
                date, approvalUser,
                ApprovalStatus.APPROVED,
                approvalUser,
                "Forex Bank",
                "This is the reason that I...",
                approvalUser,
                originalData,
                requestedChange,
                "hashed data",
                "hashed data",
                "Notes")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUser, approvalUser,
                date, approvalId,
                ApprovalRequestType.BATCH_CANCELLATION,
                "ESSESESS",
                "SEB Bank",
                "This is the reason that I...",
                approvalUser, "Notes",
                originalData, requestedChange)

        `when`(approvalRepository.findByJobId(approvalId))
                .thenReturn(approvalDetails)

        `when`(uiPresenter.presentApprovalDetails(any()))
                .thenReturn(approvalDetailsDto)

        val result = approvalFacadeImpl.getApprovalDetailsById(TestConstants.CONTEXT, ClientType.UI, approvalId)

        verify(approvalRepository).findByJobId(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentApprovalDetails(any())

        assertThat(result).isNotNull
    }
}
