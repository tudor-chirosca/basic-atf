package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.account.Account
import com.vocalink.crossproduct.domain.account.AccountRepository
import com.vocalink.crossproduct.domain.approval.Approval
import com.vocalink.crossproduct.domain.approval.ApprovalRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.routing.RoutingRecord
import com.vocalink.crossproduct.domain.routing.RoutingRepository
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDetailsDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.atMost
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ParticipantFacadeImplTest {

    private val participantRepository = mock(ParticipantRepository::class.java)
    private val accountRepository = mock(AccountRepository::class.java)
    private val approvalRepository = mock(ApprovalRepository::class.java)
    private val routingRecordRepository = mock(RoutingRepository::class.java)
    private val presenterFactory = mock(PresenterFactory::class.java)!!
    private val uiPresenter = mock(UIPresenter::class.java)!!
    private val repositoryFactory = mock(RepositoryFactory::class.java)

    private val participantFacadeImpl = ParticipantFacadeImpl(
            repositoryFactory,
            presenterFactory
    )

    @BeforeEach
    fun init() {
        `when`(repositoryFactory.getParticipantRepository(anyString()))
                .thenReturn(participantRepository)
        `when`(repositoryFactory.getAccountRepository(anyString()))
                .thenReturn(accountRepository)
        `when`(repositoryFactory.getRoutingRepository(anyString()))
                .thenReturn(routingRecordRepository)
        `when`(repositoryFactory.getApprovalRepository(anyString()))
                .thenReturn(approvalRepository)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)
    }

    @Test
    fun `should invoke presenter and repository on funding participants`() {
        val approvalPage = Page<Approval>(1, listOf(Approval.builder()
                .approvalId("10000015")
                .participantIds(listOf("ELLFSESP", "LAHYSESS"))
                .build()))
        val page = Page<Participant>(1, listOf(Participant.builder()
            .participantType(ParticipantType.FUNDING)
            .build()))
        val pageDto = PageDto<ManagedParticipantDto>(1, listOf(ManagedParticipantDto.builder()
            .participantType(ParticipantType.FUNDING)
            .build()))
        
        val request = ManagedParticipantsSearchRequest()

        `when`(participantRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(Page(1, listOf(Participant.builder().build())))

        `when`(approvalRepository.findPaginated(any()))
                .thenReturn(approvalPage)

        `when`(uiPresenter.presentManagedParticipants(any(), any()))
                .thenReturn(pageDto)

        val result = participantFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(participantRepository).findPaginated(any())
        verify(approvalRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipants(any(), any())
        verify(participantRepository).findByConnectingPartyAndType(any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke repository getRoutingRepository and findAllById for routing records on present search param in request`() {
        val approvalPage = Page<Approval>(1, listOf(Approval.builder()
                .approvalId("10000015")
                .participantIds(listOf("ELLFSESP", "LAHYSESS"))
                .build()))
        val bic = "bic"
        val page = Page<Participant>(1, listOf(Participant.builder()
            .participantType(ParticipantType.FUNDING)
            .build()))
        val pageDto = PageDto<ManagedParticipantDto>(1, listOf(ManagedParticipantDto.builder().build()))
        val routingRecord = RoutingRecord(bic, null, null, null)

        val request = ManagedParticipantsSearchRequest()
        request.q = "SEARCH"

        `when`(participantRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(Page(1, listOf(Participant.builder().build())))

        `when`(approvalRepository.findPaginated(any()))
                .thenReturn(approvalPage)

        `when`(routingRecordRepository.findAllByBic(any()))
                .thenReturn(listOf(routingRecord))

        `when`(uiPresenter.presentManagedParticipants(any(), any()))
                .thenReturn(pageDto)

        val result = participantFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(participantRepository).findPaginated(any())
        verify(approvalRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipants(any(), any())
        verify(participantRepository).findByConnectingPartyAndType(any(), any())
        verify(routingRecordRepository).findAllByBic(any())

        assertNotNull(result)
    }

    @Test
    fun `should not invoke presenter and repository when participant type is funded`() {
        val approvalPage = Page<Approval>(1, listOf(Approval.builder()
                .approvalId("10000015")
                .participantIds(listOf("ELLFSESP", "LAHYSESS"))
                .build()))
        val page = Page<Participant>(1, listOf(Participant.builder()
            .participantType(ParticipantType.FUNDED)
            .build()))
        val pageDto = PageDto<ManagedParticipantDto>(1, listOf(ManagedParticipantDto.builder().build()))
        val request = ManagedParticipantsSearchRequest()

        `when`(participantRepository.findPaginated(any()))
                .thenReturn(page)

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(Page(1, listOf(Participant.builder().build())))

        `when`(approvalRepository.findPaginated(any()))
                .thenReturn(approvalPage)

        `when`(uiPresenter.presentManagedParticipants(any(), any()))
                .thenReturn(pageDto)

        val result = participantFacadeImpl.getPaginated(TestConstants.CONTEXT, ClientType.UI, request)

        verify(participantRepository).findPaginated(any())
        verify(approvalRepository).findPaginated(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipants(any(), any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke repository getParticipantById twice on FUNDED participants`() {
        val participant = Participant.builder()
            .status(ParticipantStatus.SUSPENDED)
            .participantType(ParticipantType.FUNDED).build()
        val managedDetailsDto = ManagedParticipantDetailsDto.builder().build()
        val account = Account(null, null, null)
        val configuration = ParticipantConfiguration.builder().build()

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(Page(1, listOf(Participant.builder()
                    .status(ParticipantStatus.SUSPENDED)
                    .build())))

        `when`(approvalRepository.findPaginated(any()))
                .thenReturn(Page(0, emptyList()))

        `when`(participantRepository.findById(any()))
                .thenReturn(participant)

        `when`(accountRepository.findByPartyCode(any()))
                .thenReturn(account)

        `when`(participantRepository.findConfigurationById(any()))
                .thenReturn(configuration)

        `when`(uiPresenter.presentManagedParticipantDetails(any(), any(), any(), any(), any()))
                .thenReturn(managedDetailsDto)

        val result = participantFacadeImpl.getById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(participantRepository, atLeast(2)).findById(any())
        verify(accountRepository).findByPartyCode(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipantDetails(any(), any(), any(), any(), any())
        verify(participantRepository).findConfigurationById(any())

        assertNotNull(result)
    }

    @Test
    fun `should invoke repository getParticipantById once on FUNDING participants`() {
        val participant = Participant.builder()
            .status(ParticipantStatus.ACTIVE)
            .participantType(ParticipantType.FUNDING)
            .build()
        val managedDetailsDto = ManagedParticipantDetailsDto.builder().build()
        val account = Account(null, null, null)
        val configuration = ParticipantConfiguration.builder().build()

        `when`(participantRepository.findByConnectingPartyAndType(any(), any()))
                .thenReturn(Page(1, listOf(Participant.builder().status(ParticipantStatus.ACTIVE).build())))

        `when`(approvalRepository.findPaginated(any()))
                .thenReturn(Page(0, emptyList()))

        `when`(participantRepository.findById(any()))
                .thenReturn(participant)

        `when`(accountRepository.findByPartyCode(any()))
                .thenReturn(account)

        `when`(participantRepository.findConfigurationById(any()))
                .thenReturn(configuration)

        `when`(uiPresenter.presentManagedParticipantDetails(any(), any(), any(), any()))
                .thenReturn(managedDetailsDto)

        val result = participantFacadeImpl.getById(TestConstants.CONTEXT, ClientType.UI, "")

        verify(participantRepository, atMost(1)).findById(any())
        verify(accountRepository).findByPartyCode(any())
        verify(presenterFactory).getPresenter(any())
        verify(uiPresenter).presentManagedParticipantDetails(any(), any(), any(), any())
        verify(participantRepository).findConfigurationById(any())

        assertNotNull(result)
    }
}