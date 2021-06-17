package com.vocalink.crossproduct.ui.facade

import com.vocalink.crossproduct.RepositoryFactory
import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
import com.vocalink.crossproduct.domain.Page
import com.vocalink.crossproduct.domain.audit.AuditDetails
import com.vocalink.crossproduct.domain.audit.AuditDetailsRepository
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantRepository
import com.vocalink.crossproduct.ui.aspects.ContentUtils
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.aspects.OccurringEvent
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.PresenterFactory
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import com.vocalink.crossproduct.ui.util.TransactionHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.anyString
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.*
import java.util.function.Supplier
import org.mockito.Mockito.never
import org.springframework.transaction.CannotCreateTransactionException

class AuditFacadeImplTest {

    private val presenterFactory = mock(PresenterFactory::class.java)
    private val repositoryFactory = mock(RepositoryFactory::class.java)
    private val auditDetailsRepository = mock(AuditDetailsRepository::class.java)
    private val participantRepository = mock(ParticipantRepository::class.java)
    private val contentUtils = mock(ContentUtils::class.java)
    private val transactionHandler = mock(TransactionHandler::class.java)
    private val clock = FIXED_CLOCK

    private val uiPresenter = UIPresenter(contentUtils, clock)

    private var auditFacadeImpl = AuditFacadeImpl(repositoryFactory, presenterFactory, transactionHandler)

    companion object {
        private const val PRODUCT = "BPS"
        private const val PARTICIPANT_ID = "PART_ID"
        private const val USERNAME = "UserName"
        private var CLIENT_TYPE = ClientType.UI

        private var AUDIT_DETAILS_LIST = listOf(AuditDetails.builder()
                .id(UUID.randomUUID())
                .activityName("activityString")
                .approvalRequestId("1")
                .correlationId("corId")
                .channel("BPS")
                .username(USERNAME)
                .approvalRequestId("1")
                .requestOrResponseEnum("blah")
                .requestUrl("reqUrl")
                .contents("contentJson")
                .firstName("John")
                .lastName("Snow")
                .participantId(PARTICIPANT_ID)
                .build()
        )

        private var SERVICE_ID = 1L
    }

    @Test
    fun `should get user details`() {
        `when`(repositoryFactory.getAuditDetailsRepository(PRODUCT))
                .thenReturn(auditDetailsRepository)
        `when`(auditDetailsRepository.getAuditDetailsByParticipantIdAndGroupByUser(PARTICIPANT_ID))
                .thenReturn(AUDIT_DETAILS_LIST)
        `when`(presenterFactory.getPresenter(ClientType.UI))
                .thenReturn(uiPresenter)

        val userDetails = auditFacadeImpl.getUserDetails(PRODUCT, CLIENT_TYPE, PARTICIPANT_ID)

        assertThat(USERNAME).isEqualTo(userDetails[0].username)
    }

    @Test
    fun `audit details repository should log occurring event`() {
        `when`(repositoryFactory.getAuditDetailsRepository(anyString())).thenReturn(auditDetailsRepository)
        `when`(auditDetailsRepository.getAuditDetailsByParticipantId(PARTICIPANT_ID)).thenReturn(AUDIT_DETAILS_LIST)

        val occurringEvent = OccurringEvent.builder()
                .product(PRODUCT)
                .userId(USERNAME)
                .participantId(PARTICIPANT_ID)
                .eventType(EventType.FILE_ENQUIRY).build()

        auditFacadeImpl.handleEvent(occurringEvent)

        verify(transactionHandler).runInTransaction(any(Supplier::class.java))
        verify(auditDetailsRepository, atLeastOnce()).logOperation(any(), any())
    }

    @Test
    fun `audit details repository should never log event in database if user or participant is not found`() {
        `when`(repositoryFactory.getAuditDetailsRepository(anyString())).thenReturn(auditDetailsRepository)
        `when`(auditDetailsRepository.getAuditDetailsByParticipantId(PARTICIPANT_ID)).thenReturn(AUDIT_DETAILS_LIST)
        `when`(transactionHandler.runInTransaction(any(Supplier::class.java)))
            .thenThrow(CannotCreateTransactionException("Cannot create connection"))

        val occurringEvent = OccurringEvent.builder()
                .product(PRODUCT)
                .participantId(PARTICIPANT_ID)
                .eventType(EventType.FILE_ENQUIRY).build()

        auditFacadeImpl.handleEvent(occurringEvent)

        verify(auditDetailsRepository, never()).logOperation(any(), any())
    }

    @Test
    fun `should get audit details`() {
        val id = UUID.randomUUID()
        val correlationId = "corId"
        val request = AuditDetails.builder()
                .id(id)
                .activityName("FILE_ENQUIRY")
                .requestOrResponseEnum("REQUEST")
                .serviceId(SERVICE_ID)
                .participantId(PARTICIPANT_ID)
                .correlationId(correlationId)
                .build()
        val response = AuditDetails.builder()
                .id(id)
                .activityName("FILE_ENQUIRY")
                .requestOrResponseEnum("RESPONSE")
                .serviceId(SERVICE_ID)
                .participantId(PARTICIPANT_ID)
                .correlationId(correlationId)
                .build()
        val participant = Participant.builder()
                .bic(PARTICIPANT_ID)
                .build()

        `when`(repositoryFactory.getAuditDetailsRepository(anyString())).thenReturn(auditDetailsRepository)
        `when`(auditDetailsRepository.getAuditDetailsById(id.toString())).thenReturn(request)

        `when`(auditDetailsRepository.getAuditDetailsByCorrelationId(request.correlationId)).thenReturn(listOf(response))

        `when`(repositoryFactory.getParticipantRepository(PRODUCT)).thenReturn(participantRepository)
        `when`(participantRepository.findById(PARTICIPANT_ID)).thenReturn(participant)

        `when`(presenterFactory.getPresenter(CLIENT_TYPE)).thenReturn(uiPresenter)

        val auditDetailsDto = auditFacadeImpl.getAuditDetails(PRODUCT, CLIENT_TYPE, id.toString())

        assertThat("null-$SERVICE_ID").isEqualTo(auditDetailsDto.serviceId)
        assertThat(PARTICIPANT_ID).isEqualTo(auditDetailsDto.entity.bic)
    }

    @Test
    fun `get event type list`() {
        `when`(presenterFactory.getPresenter(CLIENT_TYPE)).thenReturn(uiPresenter)

        val eventTypes = auditFacadeImpl.getEventTypes(PRODUCT, CLIENT_TYPE)

        assertThat(eventTypes).isNotEmpty
        assertThat(eventTypes).isInstanceOf(List::class.java)
    }

    @Test
    fun `should return audit logs`() {
        `when`(repositoryFactory.getAuditDetailsRepository(anyString())).thenReturn(auditDetailsRepository)
        `when`(auditDetailsRepository.getAuditDetailsByParameters(any())).thenReturn(Page(0, emptyList()))
        `when`(presenterFactory.getPresenter(CLIENT_TYPE)).thenReturn(uiPresenter)

        val auditRequestParams = AuditRequestParams()

        val auditLogs = auditFacadeImpl.getAuditLogs(PRODUCT, CLIENT_TYPE, auditRequestParams)

        assertThat(auditLogs).isNotNull
        assertThat(auditLogs).isInstanceOf(PageDto::class.java)
    }
}
