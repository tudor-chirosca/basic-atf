package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.ServiceFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.approval.Approval
import com.vocalink.crossproduct.domain.approval.ApprovalRepository
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType
import com.vocalink.crossproduct.domain.approval.ApprovalService
import com.vocalink.crossproduct.domain.approval.ApprovalStatus
import com.vocalink.crossproduct.domain.audit.UserDetails
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus.ACTIVE
import com.vocalink.crossproduct.domain.participant.ParticipantType.FUNDED
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ApprovalFacadeImplTest {

    private val approvalRepository = mock(ApprovalRepository::class.java)!!
    private val participantRepository = mock(ParticipantRepository::class.java)!!
    private val approvalService = mock(ApprovalService::class.java)!!

    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val serviceFactory = mock(ServiceFactory::class.java)

    private val approvalFacadeImpl = ApprovalFacadeImpl(
            presenterFactory,
            repositoryFactory,
            serviceFactory
    )

    @BeforeEach
    fun init() {
        `when`(serviceFactory.getApprovalService(anyString()))
                .thenReturn(approvalService)
        `when`(repositoryFactory.getApprovalRepository(anyString()))
                .thenReturn(approvalRepository)
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on get approval details`() {
        val approvalUser = UserDetails("12a514", "John", "Doe", "P27")
        val approvalUserDto = ApprovalUserDto("John Doe", "12a514", "P27-SEK")
        val approvalId = "10000020"
        val date = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC+1"))
        val requestedChange = mapOf("status" to "Suspended")
        val originalData = mapOf("data" to "data")
        val participantReferenceDto = ParticipantReferenceDto("ESSESESS", "SEB Bank", FUNDED, ACTIVE, "P27-SEK")
        val participants = listOf(Participant.builder()
                .id("ESSESESS")
                .name("SEB Bank")
                .participantType(FUNDED)
                .schemeCode("P27-SEK")
                .build())

        val approvalDetails = Approval(
                approvalId,
                ApprovalRequestType.PARTICIPANT_SUSPEND,
                listOf("ESSESESS"),
                date, approvalUser,
                ApprovalStatus.APPROVED,
                approvalUser,
                "This is the reason that I...",
                approvalUser,
                originalData,
                requestedChange,
                "hashed data",
                "hashed data",
                "Notes")

        val approvalDetailsDto = ApprovalDetailsDto(
                ApprovalStatus.APPROVED,
                approvalUserDto, approvalUserDto,
                date, approvalId,
                ApprovalRequestType.BATCH_CANCELLATION,
                listOf(participantReferenceDto),
                "This is the reason that I...",
                approvalUserDto, "Notes",
                originalData, requestedChange)

        `when`(approvalRepository.findByJobId(approvalId))
                .thenReturn(approvalDetails)

        `when`(participantRepository.findAll())
                .thenReturn(Page(1, participants))

        `when`(uiPresenter.presentApprovalDetails(any(), any()))
                .thenReturn(approvalDetailsDto)

        val result = approvalFacadeImpl.getApprovalDetailsById(TestConstants.CONTEXT, ClientType.UI, approvalId)

        verify(approvalRepository).findByJobId(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentApprovalDetails(any(), any())

        assertThat(result).isNotNull
    }

    @Test
    fun `should invoke presenter and repository on get approvals`() {
        val page = Page<Approval>(1, listOf(Approval(null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null)))
        val pageDto = PageDto<ApprovalDetailsDto>(1, listOf(ApprovalDetailsDto(null,
            null, null, null, null, null, null,
            null, null, null, null, null)))
        val request = ApprovalSearchRequest()

        `when`(approvalRepository.findPaginated(any()))
            .thenReturn(page)

        `when`(participantRepository.findAll())
                .thenReturn(Page(0, emptyList()))

        `when`(uiPresenter.presentApproval(any(), any()))
            .thenReturn(pageDto)

        val result = approvalFacadeImpl.getApprovals(TestConstants.CONTEXT, ClientType.UI, request)

        verify(approvalRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentApproval(any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke presenter and repository on request approval`() {
        val approval = Approval(null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null)

        val approvalDetailsDto = ApprovalDetailsDto(null,
            null, null, null, null, null, null,
            null, null, null, null, null)

        val request = ApprovalChangeRequest("PARTICIPANT_SUSPEND", mapOf("status" to "suspended"),"notes")

        `when`(approvalRepository.requestApproval(any()))
            .thenReturn(approval)

        `when`(participantRepository.findAll())
                .thenReturn(Page(0, emptyList()))

        `when`(uiPresenter.presentApprovalDetails(any(), any()))
            .thenReturn(approvalDetailsDto)

        val result = approvalFacadeImpl.requestApproval(TestConstants.CONTEXT, ClientType.UI, request)

        verify(approvalRepository).requestApproval(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentApprovalDetails(any(), any())

        assertNotNull(result)
    }
}
